import * as React from 'react';
import { Routes, Route } from 'react-router-dom';
import './App.css'
import Login from './components/Login/Login'
import Dashboard from './components/Dashboard/Dashboard'
import Donors from './components/Donors/Donors';
import Donations from './components/Donations/Donations';
import Category from './components/Category/Category';
import PageNotFound from './components/PageNotFound/PageNotFound';
import Causes from './components/Causes/Causes';
import OptionsGroup from './components/OptionsGroup/OptionsGroup';
import Options from './components/Options/Options';
function App() {

  return (
    <>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />}>
          <Route index element={<div>Welcome to Admin Dashboard</div>} />
          <Route path="donors" element={<Donors />} />
          <Route path="donations" element={<Donations />} />
          <Route path="causes" element={<Causes />} />
          <Route path="options" element={<Options />} />
          <Route path="options-group" element={<OptionsGroup />} />
          <Route path="category" element={<Category />} />
      </Route>
        <Route path="*" element={<PageNotFound/>} />
      </Routes>
    </>
  )
}

export default App
