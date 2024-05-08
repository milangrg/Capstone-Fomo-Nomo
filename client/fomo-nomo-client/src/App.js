import { Routes, Route } from "react-router-dom";
import { BrowserRouter as Router } from "react-router-dom";
import InviteList from './InviteList';
import EventList from './EventList';
import Home from './Home';
import NavBar from './NavBar';

function App() {
  return (
    <div className="App">
      <Router>
      <header className="banner">
        <img src="bannerphoto.png" alt="Banner" className='banner'/>
      </header>
      <NavBar/>        
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/invites/' element={<InviteList />} />
          <Route path='/events/' element={<EventList />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
