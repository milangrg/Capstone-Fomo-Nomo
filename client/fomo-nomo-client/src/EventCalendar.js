import React from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import style from 'react-big-calendar/lib/css/react-big-calendar.css';
import { useState, useEffect } from 'react';
import EventInfo from './EventInfo';
import EventForm from './EventForm';

const localizer = momentLocalizer(moment);

// const events = [
//   {
  
//       "eventId": 5,
//       "host": {
//         "userId": 1,
//         "firstName": "Alex",
//         "lastName": "Carter",
//         "email": "acarter1234@emailspot.com",
//         "phone": "555-555-1234",
//         "dob": "1995-08-02"
//       },
//       "title": "Golf Outing",
//       "description": "It is time for our annual golf tournament!",
//       "location": {
//         "locationId": 7,
//         "address": "1550 Golf Path",
//         "state": "NY",
//         "city": "Saratoga Springs",
//         "postal": "12866",
//         "locationName": "The Golf Emporium"
//       },
//       "eventType": "SOCIAL",
//       "start": "2024-05-11T01:00:00",
//       "end": "2024-05-11T05:00:00"
//     },

//     {"eventId": 3,
//     "host": {
//       "userId": 9,
//       "firstName": "Viola",
//       "lastName": "Reynolds",
//       "email": "vr11_@yahoo2.com",
//       "phone": "555-555-9999",
//       "dob": "1989-05-19"
//     },
//     "title": "Work Happy Hour",
//     "description": "Meet for happy hour drinks and food",
//     "location": {
//       "locationId": 1,
//       "address": "3300 Riverfront Walk",
//       "state": "NY",
//       "city": "Buffalo",
//       "postal": "14202",
//       "locationName": "Riverside Restaurant"
//     },
//     "eventType": "WORK",
//     "start": "2024-05-21T10:00:00",
//     "end": "2024-05-21T13:00:00"
//   },


// ]
const holidays = [
{
  id: 1,
  title: 'Christmas',
  start: new Date(2024, 4, 11),
  end: new Date(2024, 4, 11),
  description: 'hohoho',
  list: 'blah blah'
},

{
  id: 2,
  title: 'Birthday',
  start: new Date(2024, 4, 12, 0, 0),
  end: new Date(2024, 4, 12, 23, 59),
  description: '31 and having fun', 
  list: 'blah blah'
},

]



// need functions to pull events i'm hosting or i've accepted only for this page and eventlist page

const EventCalendar = () => {

  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [selectedSlot, setSelectedSlot] = useState(null);


  const url = 'http://localhost:8080/api/invitation/1';

  useEffect(() => {
    fetch(url)
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                return Promise.reject(`Unexpected status code: ${response.status}`);
            }
        })
        .then(data => setEvents(data))
        .catch(console.log)
}, []);


  const formattedEvents = events.map(e => ({
    ...e, 
    start: new Date(e.start),
    end: new Date(e.end)
  }))

  console.log(formattedEvents)
  

  const handleEventSelect = event => {
    setSelectedEvent(event);
  };

  const closePopup = () => {
    setSelectedEvent(null);
    setSelectedSlot(null)
  };


  const handleSlotSelect = slotInfo => {
    console.log(`Selected slot from ${slotInfo.start} to ${slotInfo.end}`);
    setSelectedEvent(null);  

    setSelectedSlot({
        "eventId": 0,
        "host": {
            "userId": 1,
            "firstName": "Alex",
            "lastName": "Carter",
            "email": "acarter1234@emailspot.com",
            "phone": "555-555-1234",
            "dob": "1995-08-02"
        },
        "title": "",
        "description": "",
        "location": {
            "locationId": 0,
            "address": "",
            "state": "",
            "city": "",
            "postal": "",
            "locationName": ""
        },
        "eventType": "",
        "start": new Date(slotInfo.start),
        "end":new Date(slotInfo.end)
    }
    );
};


  const eventColor = (type) => {

    switch(type) {
      case 'PERSONAL':
        return '#471ca8';
      case 'SOCIAL':
        return '#884ab2';
      case 'APPOINTMENT':
          return '#ff930a';
      case 'WORK':
          return '#f24b04';
      default:
        return '#d1105a';
    }
  
  }



  return (
    <div className='calendar-container'>
    <div style={{ minHeight: '700px' }}>
      <Calendar
        localizer={localizer}
        events={formattedEvents.concat(holidays)}
        startAccessor="start"
        endAccessor="end"
        selectable
        style={{ minHeight: '500px', style, fontFamily: 'Urbanist'}}
        // set color of event 
        eventPropGetter={e => {
          const backgroundColor = eventColor(e.eventType);          
          return {style: {
            backgroundColor}};
        }}

        onSelectEvent={handleEventSelect}
        onSelectSlot={handleSlotSelect}
      />
      {selectedEvent && <EventInfo event={selectedEvent} onClose={closePopup} fromInvite={false} />}
      {selectedSlot && <EventForm event={selectedSlot} onClose={closePopup} />}
    </div>
    </div>
  );
};

export default EventCalendar;