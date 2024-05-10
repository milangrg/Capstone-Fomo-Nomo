import React from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import style from 'react-big-calendar/lib/css/react-big-calendar.css';
import { useState, useEffect } from 'react';
import EventInfo from './EventInfo';
import EventForm from './EventForm';

const localizer = momentLocalizer(moment);


const EventCalendar = ({holidayCalendar}) => {

  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [holidays, setHolidays] = useState(holidayCalendar);


  const url = 'http://localhost:8080/api/invitation/1';


  const fetchEvents = () => {
    fetch(url)
      .then(response => response.json())
      .then(data => {
        setEvents(data);
      })
      .catch(console.error);
  };

  useEffect(() => {
    setHolidays(holidayCalendar);
    fetchEvents();
  }, []);

  const formattedEvents = events.map(e => ({
    ...e,
    start: new Date(e.start),
    end: new Date(e.end)
  }))


  const handleEventSelect = event => {
    setSelectedEvent(event);
  };

  const closePopup = () => {
    setSelectedEvent(null);
    setSelectedSlot(null)
    fetchEvents();
  };


  const handleSlotSelect = slotInfo => {
    console.log(`Selected slot from ${slotInfo.start} to ${slotInfo.end}`);
    setSelectedEvent(null);

    setSelectedSlot({
      "eventId": 0,
      "host": {
        "userId": 1,
        "firstName": "You",
        "lastName": "Zer",
        "email": "youzer@emailspot.com",
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
      "end": new Date(slotInfo.end)
    }
    );
  };


  const eventColor = (type) => {

    switch (type) {
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
    <>
      <div className='calendar-container'>
        <div style={{ minHeight: '700px' }}>
          <Calendar
            localizer={localizer}
            events={formattedEvents.concat(holidays)}
            startAccessor="start"
            endAccessor="end"
            selectable
            style={{ height: '65vh', style, fontFamily: 'Urbanist' }}
            // set color of event 
            eventPropGetter={e => {
              const backgroundColor = eventColor(e.eventType);
              return {
                style: {
                  backgroundColor
                }
              };
            }}

            onSelectEvent={handleEventSelect}
            onSelectSlot={handleSlotSelect}
          />
          {selectedEvent && <EventInfo event={selectedEvent} onClose={closePopup} fromInvite={false} />}
          {selectedSlot && <EventForm event={selectedSlot} onClose={closePopup} />}
        </div>
      </div>
    </>
  );
};

export default EventCalendar;