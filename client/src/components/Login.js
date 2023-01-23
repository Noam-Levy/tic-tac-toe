import React from 'react';
import PropTypes from 'prop-types';
import { useForm } from 'react-hook-form';
import styled from 'styled-components';

import Button from '@mui/material/Button';

import ControlledTextField from './ControlledTextField';
import { createNewGame, joinGame } from '../gameApi';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  background-color: #867b96;
  border-radius: 1rem;
  padding: 2rem;
`;

function Login({ handleLogin }) {
  const { control, handleSubmit, formState: { errors }} = useForm();

  const handleJoinGame = (formData) => {
    console.log(`JOIN GAME: ${JSON.stringify(formData)}`);
    joinGame(formData)
      .then((res) => handleLogin(res))
      .catch((err) => console.log(err.message));
  }

  const handleCreateNewGame = (formData) => {
    console.log(`CREATE GAME: ${JSON.stringify(formData)}`);
    createNewGame(formData.name)
      .then((res) => handleLogin(res))
      .catch((err) => console.log(err.message));

  }

  return (
    <Container>
      <h1 style={{ color: 'white' }}>Tic Tac Toe</h1>
      <Form>
      <ControlledTextField 
        name='name'
        label='Player name'
        rules={{ required: true }}
        errors={errors}
        control={control}
      />
      <ControlledTextField 
        name='gameId'
        label='Game ID'
        errors={errors}
        control={control}
      />
      <Button 
        variant='contained'
        color='secondary'
        onClick={handleSubmit(handleCreateNewGame)}
        sx={{ margin: '0.5rem'}}
      >
        Create new game
      </Button>
      <Button 
         onClick={handleSubmit(handleJoinGame)}
        variant='contained'
        color='secondary'
        sx={{ margin: '0.5rem'}}
      >
        Join a game
      </Button>
      </Form>
    </Container>
  )
}

Login.propTypes = {
  handleLogin: PropTypes.func,
};

Login.defaultProps = {
  handleLogin: () => {},
};


export default Login;
