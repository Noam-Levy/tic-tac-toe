import React from 'react';
import PropTypes from 'prop-types';

import IconButton from '@mui/material/IconButton';
import MUIAlert from '@mui/material/Alert';
import Collapse from '@mui/material/Collapse';
import CloseIcon from '@mui/icons-material/Close';

function Alert({ isDisplayed, onClick, variant, severity, message }) {
  return (
    <Collapse in={isDisplayed}>
      <MUIAlert
        action={(
          <IconButton
            aria-label="close"
            size="small"
            onClick={onClick}
          >
            <CloseIcon fontSize="inherit" />
          </IconButton>
        )}
        variant={variant}
        severity={severity}
        sx={{ maxWidth: '90%', margin: '0.5rem', textAlign: 'center' }}
      >
        {message}
      </MUIAlert>
    </Collapse>
  );
}

Alert.propTypes = {
  isDisplayed: PropTypes.bool,
  onClick: PropTypes.func,
  variant: PropTypes.string,
  severity: PropTypes.string,
  message: PropTypes.string,
};

Alert.defaultProps = {
  isDisplayed: false,
  onClick: () => {},
  variant: 'standard',
  severity: 'error',
  message: 'Something went wrong...',
};

export default Alert;
