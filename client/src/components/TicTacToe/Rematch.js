import React from 'react';
import styled from 'styled-components';
import PropTypes from 'prop-types';

import Button from '@mui/material/Button';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fcfcfc;
  margin-bottom: 1rem;
`;

const HorizontalContainer = styled(Container)`
  flex-direction: row;
  gap: 0.5rem;
`;

function Rematch({text, onAccept, onReject}) {
  return (
    <Container>
          <span style={{ margin: '1rem' }}>{text}</span>
          <HorizontalContainer>
            <Button variant='contained' color='success' onClick={() => onAccept()}>YES</Button>
            <Button variant='contained' color='error' onClick={() => onReject()}>NO</Button>
          </HorizontalContainer>
        </Container>
  );
};

Rematch.propTypes = {
  text: PropTypes.string,
  onAccept: PropTypes.func,
  onReject: PropTypes.func,
};

Rematch.defaultProps = {
  text: 'Another Go?',
  onAccept: () => {},
  onReject: () => {},
};

export default Rematch;
