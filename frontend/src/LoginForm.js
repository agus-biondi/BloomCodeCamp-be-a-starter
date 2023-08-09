import React, {
	useState, useEffect
} from 'react';
import styled, {
	keyframes
} from 'styled-components';
import axios from 'axios';
import {
	useNavigate
} from 'react-router-dom';


const rotate = keyframes`
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
`;

const Page = styled.div`
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  background: linear-gradient(120deg, #0d1117, #161b22);
  position:relative;
`;

const LoginForm = styled.div`
  width: 300px;
  padding: 0px 60px 25px;
  background: #242526;
  border-radius: 10px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 50px;
`;

const Input = styled.input`
  width: 90%;
  padding: 10px;
  margin: 10px 0;
  border: none;
  border-radius: 5px;
  background: #3a3b3c;
  color: white;
  outline: none;
  transition: transform 0.2s;
`;

const Button = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 97%;
  padding: 10px;
  margin: 10px 0;
  border: none;
  border-radius: 5px;
  background: #007a99;
  color: white;
  cursor: pointer;
  transition: background 0.3s;
  &:hover {
    background: #0099cc;
  }
`;

const LoadingIcon = styled.div`
  border: 2px solid #f3f3f3;
  border-top: 2px solid #3498db;
  border-radius: 50%;
  width: 12px;
  height: 12px;
  animation: ${rotate} 2s linear infinite;
  margin-left: 5px;
`;


const LogoImage = styled.img`
  width: 200px;
  margin-bottom: -20px
`;

const TitleImage = styled.img`
  width: 650px;
  margin-top: -20px
`;

const ErrorMessage = styled.p`
  color: red;
  font-weight: bold;
`;

const LoginPage = () => {
	const navigate = useNavigate();
	const [loading, setLoading] = useState(false);
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        console.log("mounted");
        const token = localStorage.getItem('jwt');

        if (token) {
            setLoading(true);
            axios.get('http://localhost:8080/api/auth/validate', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(response => {
                if (response.data) {
                    setLoading(false);
                    navigate('/dashboard');
                }
            })
            .catch(err => {
                localStorage.removeItem('jwt');
            })
            .finally(() => {
                setLoading(false);
            });
        }
    });

	const handleLogin = async (event) => {
		setLoading(true);
		setErrorMessage('');
		event.preventDefault();

		try {
			const response = await axios.post('http://localhost:8080/api/auth/login', {
				username: username,
				password: password
			});

			const token = response.data.token;
			console.log(token);

			localStorage.setItem('jwt', token);

			navigate('/dashboard');
		} catch (error) {
			if (error.response) {
				switch (error.response.status) {
					case 401:
						setErrorMessage('Incorrect username or password.');
						break;
					default:
						setErrorMessage('An error occurred. Please try again.');
						break;
				}
			} else {
				setErrorMessage('An error occurred, and I refuse to give more information');
			}
		} finally {
			setLoading(false);
		}
	};
    return (

      <Page>





        <LoginForm>
                <LogoImage src="images/bloom_icon_no_bg.png" />
          <TitleImage src="images/bloom_title_w_tagline.png" />
          <form onSubmit={handleLogin}>
            <Input type="text" placeholder="Username" onChange={e => setUsername(e.target.value)} value={username} autoComplete="username"/>
            <Input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} value={password} autoComplete="current-password"/>
            <Button type="submit">
              {loading ? <LoadingIcon /> : "Login"}
            </Button>
          {errorMessage && <ErrorMessage>{errorMessage}</ErrorMessage>}
          </form>
        </LoginForm>
      </Page>
    );

}

export default LoginPage;