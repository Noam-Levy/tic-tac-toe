import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';

import Backdrop from '@mui/material/Backdrop';
import CircularProgress from '@mui/material/CircularProgress';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const Text = styled.span`
  text-align: center;
  text-size: 1rem;
  color: #efefef;
`;


function Loader({ text }) {
  return (
    <Backdrop open>
      <Container>
        <CircularProgress color='secondary' size='80px' sx={{ margin: '2rem'}} />
        <Text>{text}</Text>
      </Container>
    </Backdrop>
  )
};

Loader.propTypes = {
  text: PropTypes.string,
};

Loader.defaultProps = {
  text: 'Loading...'
};

export default Loader;
