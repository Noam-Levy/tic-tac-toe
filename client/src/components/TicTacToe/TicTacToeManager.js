import React, { useEffect, useState } from 'react'
import styled from 'styled-components';
import PropTypes from 'prop-types';

import { Stomp } from '@stomp/stompjs'
import SockJS from 'sockjs-client';

import { API_BASE_URL, doTurn, rematch } from '../../gameApi';
import Board from './Board';
import Rematch from './Rematch';
import Loader from '../common/Loader';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fcfcfc;
  margin-bottom: 1rem;
`;


function TicTacToeManager({ player, gameData, setGameData, handleError }) {
  var stompClient, socket;
  const [ showRematch, setShowRematch ] = useState(false);

  useEffect(() => {
    if (gameData.status === "FINISHED") {
      setGameData(undefined);
      handleError('One of the players has declined the rematch.');
    }
    else if (gameData.status === "IN_PROGRESS") {
      initializeConnection(`${API_BASE_URL}/doTurn`, `/topic/progress/${gameData.gameId}`, (response) => { setGameData(JSON.parse(response.body)) });
      setShowRematch(true);
    }
    else if (gameData.status !== "WAITING")
      initializeConnection(`${API_BASE_URL}/connect`, `/topic/connection/${gameData.gameId}`, (response) => { setGameData(JSON.parse(response.body)) });
  }, [gameData]); //eslint-disable-line react-hooks/exhaustive-deps

  const handleCellSelection = (cell) => {
    const [x, y] = cell.split("_");
    doTurn({'gameId': gameData.gameId, 'player': player, 'coordinates': { x, y }})
      .then((newGameData) => setGameData(newGameData))
      .catch((err) => handleError(err.message));
  };

  const initializeConnection = (socketURL, subscriptionURL, subscriptionCallback) => {
    socket = new SockJS(socketURL);
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {}; // disable debug logging
    stompClient.connect({}, () => {
      stompClient.subscribe(subscriptionURL, (response) => { subscriptionCallback(response); });
    });
  }

  const renderGameBoard = () => {
    if (gameData?.status  !== "NEW")
      return <Board gameBoard={gameData?.board} onSelection={handleCellSelection}/>
    return <Loader text={`Waiting for opponent to join game. (ID: ${gameData?.gameId}) `} /> 
  };

  const onRematchDecision = (decision) => {
    setShowRematch(false);
    player.acceptRematch = decision;
    rematch({player, gameId: gameData.gameId})
      .catch((err) => handleError(err.message));
  };

  return (
    <Container>
      <Container>
        <span>{`Now Playing: ${gameData.turnsPlayed % 2 === 0 ? gameData.p1.name : gameData.p2.name}`}</span>
        <span>{`Game ID: ${gameData?.gameId}`}</span>
        <span>{`Player name: ${player.name}, Sign: ${player.sign}`}</span>
      </Container>
      { renderGameBoard() }
      { gameData?.status  === "WAITING" && <span>{gameData?.winner?.name ? `WINNER: ${gameData?.winner?.name}` : "Draw"}</span> }
      { gameData?.status  === "WAITING" && showRematch && <Rematch onAccept={() => onRematchDecision(true)} onReject={() => onRematchDecision(false)} /> }
    </Container>
  );
};

TicTacToeManager.propTypes = {
  player: PropTypes.shape({}),
  gameData: PropTypes.shape({}),
  setGameData: PropTypes.func,
  handleError: PropTypes.func,
};

TicTacToeManager.defaultProps = {
  player: {},
  gameData: {},
  setGameData: () => {},
  handleError: () => {},
};

export default TicTacToeManager;
