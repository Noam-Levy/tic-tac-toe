import axios from 'axios';

const API_BASE_URL = `http://localhost:8080/tictactoe`;

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {'Content-Type': 'application/json'},
  responseType: 'json',
});

export const createNewGame = async (player) => (
  api.post('/init', player)
    .then((result) => result.data)
    .catch((err) => { throw new Error(err.response.data.message) })
);

export const joinGame = async (data) => {
  const {name, gameId} = data;

  return api.post(`/connect`, name, { params: { gameId }})
    .then((result) => result.data)
    .catch((err) => { throw new Error(err.response.data.message) })
};


export const doTurn = async (turn) => {
  return api.post('/doTurn', turn)
    .then((result) => result.data)
    .catch((err) => { throw new Error(err.response.data.message) })
};
