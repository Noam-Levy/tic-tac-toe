import axios from 'axios';

export const API_BASE_URL = `http://localhost:8080`;

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {'Content-Type': 'application/json'},
  responseType: 'json',
});

export const createNewGame = async (player) => (
  api.post('/tictactoe/init', player)
    .then((result) => result.data)
    .catch((err) => { throw new Error(err.response.data.message) })
);

export const joinGame = async (data) => {
  const {name, gameId} = data;
  return api.post(`/tictactoe/connect`, name, { params: { gameId }})
    .then((result) => result.data)
    .catch((err) => { throw new Error(err.response.data.message) })
};


export const doTurn = async (turn) => {
  return api.post('/tictactoe/doTurn', turn)
    .then((result) => result.data)
    .catch((err) => { throw new Error(err.response.data.message) })
};

export const rematch = async (data) => {
  const { player, gameId } = data;
  return api.post('/tictactoe/rematch', player, { params: { gameId }})
    .then((result) => result.data)
    .catch((err) => { throw new Error(err.response.data.message) });
};
