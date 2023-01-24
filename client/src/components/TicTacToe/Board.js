import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';

const Container = styled.div`
  width: 270px;
  margin: 40p auto;
  overflow: auto;
  background: #d9d0cf;
  border-radius: 1rem;
  margin-bottom: 1rem;
`;

const Cell = styled.li`
    float: left;
    margin: 10px;
    height: 70px;
    width: 70px;
    font-size: 50px;
    background: #333;
    color: #cecece;
    list-style: none;
    text-align: center;
    border-radius: 5px;

    :hover {
      cursor: pointer;
      background: #000
    }
`;

const X = () => (<span style={{ color: 'green', width: '100%', height: '100%' }}>X</span>)
const O = () => (<span style={{ color: 'red', width: '100%', height: '100%' }}>O</span>)

function Board({gameBoard, onSelection}) {

  const renderRow = (row, rowIndex) => (row.map((cell, cellIndex) => {
    return <Cell 
              key={`${rowIndex}_${cellIndex}`}
              onClick={() => onSelection(`${rowIndex}_${cellIndex}`)}
            >
              {renderCellValue(cell)}
            </Cell>
  }));

  const renderCellValue = (value) => {
    if (value === 0)
      return;
    if (value === 1)
      return <X />
    return <O />
  };

  return (
    <Container>
      { gameBoard.map((row, rowIndex) => (renderRow(row, rowIndex))) }
    </Container>
  )
};

Board.propTypes = {
  gameBoard: PropTypes.array,
  onSelection: PropTypes.func,
};

Board.defaultProps = {
  gameBoard: [],
  onSelection: () => {},
};

export default Board;
