import { Routes, Route } from "react-router-dom";
import { BrowserRouter as Router } from "react-router-dom";
import InviteList from './InviteList';
import EventList from './EventList';
import Home from './Home';
import NavBar from './NavBar';
import { useState, useEffect } from 'react';

function App() {

  const [holidays, setHolidays] = useState([]);
  
  useEffect(() => {

    fetchData();

  }, []);


  const fetchData = async () => {
    try {
      const requestOptions = {
        method: 'GET',
        headers: {
          'X-Api-Key': 'PMNsWB2gYDmLxBDJOwyGtg==Njf1JODiChschKfM',
        },
        contentType: 'application/json',
      };
      const response2 = await fetch('https://api.api-ninjas.com/v1/holidays?country=US&year=2024&type=state_holiday', requestOptions);
      const data2 = await response2.json();
      // console.log('APP ONLY')

      const formattedHolidays = data2.map(holiday => ({
        ...holiday, 
        title: holiday.name,
        start: `${holiday.date}T00:00`,
        end:`${holiday.date}T01:00`
      }))
      setHolidays(formattedHolidays);

    } catch (error) {
      console.error('ERROR:', error);
    }
  };

  if (!holidays.length) return <div>Loading...</div>

  return (
    <div className="App">
      <Router>
        <header className="banner">
          <img src="bannerphoto.png" alt="Banner" className='banner' />
        </header>
        <NavBar />
        <Routes>
          <Route path='/' element={<Home holidayCalendar={holidays} />} />
          <Route path='/invites/' element={<InviteList />} />
          <Route path='/events/' element={<EventList />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
