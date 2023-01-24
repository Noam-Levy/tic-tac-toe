import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';

import Button from '@mui/material/Button';

import ControlledTextField from './common/ControlledTextField';
import { createNewGame, joinGame } from '../gameApi';

const Form = styled.form`
  display: flex;
  flex-direction: column;
  background-color: #d9d0cf;
  border-radius: 1rem;
  padding: 1rem;
  margin-bottom: 2rem;
`;

function Login({ handleLogin, handleError }) {
  const { control, handleSubmit, formState: { errors }} = useForm();

  const handleJoinGame = (formData) => {
    joinGame(formData)
      .then((res) => handleLogin({player: 'p2', gameData: res}))
      .catch((err) => handleError(err.message));
  }

  const handleCreateNewGame = (formData) => {
    createNewGame(formData.name)
      .then((res) => handleLogin({player: 'p1', gameData: res}))
      .catch((err) => handleError(err.message));
  }

  return (
      <Form>
        <ControlledTextField 
          name='name'
          label='Player name'
          rules={{ required: true }}
          errors={errors}
          control={control}
          autoFocus
        />
        <ControlledTextField 
          name='gameId'
          label='Game ID'
          errors={errors}
          control={control}
          placeholder={'leave empty to join randomly'}
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
  )
}

Login.propTypes = {
  handleLogin: PropTypes.func,
  handleError: PropTypes.func,
};

Login.defaultProps = {
  handleLogin: () => {},
  handleError: () => {},
};


export default Login;
