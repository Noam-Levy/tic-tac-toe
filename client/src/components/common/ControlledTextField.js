import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Controller } from 'react-hook-form';

import TextField from '@mui/material/TextField';
import FormControl from '@mui/material/FormControl';

const errorTypeTexts = { required: 'This field is required' };

const StyledTextField = styled(TextField)({
  '& .MuiOutlinedInput-root': {
    backgroundColor: 'white',
    maxWidth: '80%',
    minWidth: '240px',
    '& .Mui-focused': {
      borderColor: 'black'
    },
  },
});

function ControlledTextField(props) {
  const { control, name, label, type, errors, rules, defaultValue, placeholder, autoFocus } = props;
  

  return (
    <Controller 
      name={name}
      control={control}
      rules={rules}
      defaultValue={defaultValue}
      render={
        ({ field }) => (
          <FormControl>
            <span>{label}</span>
            <StyledTextField
              {...field}
              size="small"
              type={type}
              margin="normal"
              error={!!errors[name]}
              helperText={errorTypeTexts[errors[name]?.type]}
              placeholder={placeholder}
              autoFocus={autoFocus}
            />
          </FormControl>
        )
      }
    />
  )
}

ControlledTextField.propTypes = {
  control: PropTypes.shape({}),
  name: PropTypes.string,
  label: PropTypes.string,
  type: PropTypes.string,
  errors: PropTypes.shape({}),
  rules: PropTypes.shape({}),
  defaultValue: PropTypes.string,
  placeholder: PropTypes.string,
  autoFocus: PropTypes.bool,
};

ControlledTextField.defaultProps = {
  control: {},
  name: '',
  label: '',
  type: 'text',
  errors: {},
  rules: {},
  defaultValue: '',
  placeholder: '',
  autoFocus: false,
};

export default ControlledTextField
