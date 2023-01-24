/* eslint no-unused-vars: off */
import React, { useState } from "react";
import styled from 'styled-components';

import Login from "./components/Login";
import TicTacToeManager from "./components/TicTacToe/TicTacToeManager";
import Alert from "./components/common/Alert";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

function App() {
  const [alertMessage, setAlertMessage] = useState();
  const [isShowAlert, setIsShowAlert] = useState(false);
  const [player, setPlayer] = useState();
  const [game, setGame] = useState();

  const handleLogin = (data) => {
    const { player, ...gameData } = data;
    setPlayer(gameData.gameData[player]);
    setGame(gameData.gameData);
  }

  const handleError = (errorMessage) => {
    setAlertMessage(errorMessage);
    setIsShowAlert(true);
    setTimeout(() => setIsShowAlert(false), 5000); // auto close alert window after 5 seconds
  }

  return (
    <Container>
      <h1 style={{ color: '#d9d0cf' }}>Tic Tac Toe</h1>
      {
        (player && game) ? <TicTacToeManager player={player} gameData={game} setGameData={setGame} handleError={handleError} /> : (
          <Login handleLogin={handleLogin} handleError={handleError} />
        )
      }
      <Alert
        isDisplayed={isShowAlert}
        variant="filled"
        onClick={() => setIsShowAlert(false)}
        message={alertMessage}
      />
    </Container>
  )
}

export default App;
