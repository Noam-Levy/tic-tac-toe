/* eslint no-unused-vars: off */
import React, { useState } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import Login from "./components/Login";
import GameBoard from "./components/GameBoard";
import ErrorPage from "./components/ErrorPage";

function App() {
  const [user, setUser] = useState();

  const router = createBrowserRouter([
    {
      path: "/",
      element: <App />,
      errorElement: <ErrorPage />,
    }
  ]);

  return user ? <GameBoard /> : <Login />;
}

export default App;
