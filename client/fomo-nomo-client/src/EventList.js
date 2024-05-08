import EventInfo from "./EventInfo";
import EventForm from "./EventForm";
import moment from 'moment';
import { useState, useEffect } from 'react';


// const events = [
//     {

//         id: 6,
//         hostId: 2,
//         title: 'Team Meeting',
//         startDate: new Date(2024, 4, 7, 10, 0),
//         endDate: new Date(2024, 4, 7, 11, 0),
//         description: 'Discuss project updates and deadlines.',
//         type: 'Work',
//         attendees: ['Doug', 'Joe', 'Me']
//     },
//     {
//         id: 1,
//         hostId: 1,
//         title: 'Doctor Appointment',
//         startDate: new Date(2024, 4, 8, 16, 30),
//         endDate: new Date(2024, 4, 8, 17, 30),
//         description: 'Annual check-up',
//         type: 'Appointment'
//     },
//     {
//         id: 2,
//         hostId: 1,
//         title: 'Lunch with Sarah',
//         startDate: new Date(2024, 4, 9, 12, 0),
//         endDate: new Date(2024, 4, 9, 13, 0),
//         description: 'Meeting friend for lunch',
//         type: 'Social',
//         attendees: ['Sarah', 'Me']
//     },
//     {
//         id: 3,
//         hostId: 2,
//         title: 'Webinar on React',
//         startDate: new Date(2024, 4, 10, 15, 0),
//         endDate: new Date(2024, 4, 10, 16, 0),
//         description: 'Attend an online webinar about React',
//         type: 'Work',
//         attendees: ['Sarah', 'Me']
//     },
//     {
//         id: 4,
//         hostId: 1,
//         title: 'Gym',
//         startDate: new Date(2024, 4, 11, 18, 30),
//         endDate: new Date(2024, 4, 11, 20, 0),
//         description: 'Workout session',
//         type: 'Personal'
//     }
// ];

// need functions to pull events i'm hosting or i've accepted only for this page and eventcalendar page

const defaultEvent = {
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
    "start": new Date(),
    "end": new Date()
}

const events = [
    {
    
        "eventId": 5,
        "host": {
          "userId": 1,
          "firstName": "Alex",
          "lastName": "Carter",
          "email": "acarter1234@emailspot.com",
          "phone": "555-555-1234",
          "dob": "1995-08-02"
        },
        "title": "Golf Outing",
        "description": "It is time for our annual golf tournament!",
        "location": {
          "locationId": 7,
          "address": "1550 Golf Path",
          "state": "NY",
          "city": "Saratoga Springs",
          "postal": "12866",
          "locationName": "The Golf Emporium"
        },
        "eventType": "SOCIAL",
        "start": "2024-05-11T01:00:00",
        "end": "2024-05-11T05:00:00"
      },

      {"eventId": 3,
      "host": {
        "userId": 9,
        "firstName": "Viola",
        "lastName": "Reynolds",
        "email": "vr11_@yahoo2.com",
        "phone": "555-555-9999",
        "dob": "1989-05-19"
      },
      "title": "Work Happy Hour",
      "description": "Meet for happy hour drinks and food",
      "location": {
        "locationId": 1,
        "address": "3300 Riverfront Walk",
        "state": "NY",
        "city": "Buffalo",
        "postal": "14202",
        "locationName": "Riverside Restaurant"
      },
      "eventType": "WORK",
      "start": "2024-05-21T10:00:00",
      "end": "2024-05-21T13:00:00"
    },


  ]

function EventList() {


    // const [events, setEvents] = useState();
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [showEventForm, setShowEventForm] = useState(false);

    // const url = 'http://localhost:8080/api/events';

    // useEffect(() => {
    //     fetch(url)
    //         .then(response => {
    //             if (response.status === 200) {
    //                 return response.json();
    //             } else {
    //                 return Promise.reject(`Unexpected status code: ${response.status}`);
    //             }
    //         })
    //         .then(data => setEvents(data))
    //         .catch(console.log)
    // }, []);

    const handleEventSelect = (e) => () => {
        console.log(e.title)
        setSelectedEvent(e);

    };

    const handleNewEventClick = () => {
        setShowEventForm(true);
    };

    const closePopup = () => {
        setSelectedEvent(null);
    };

    const closeEventForm = () => {
        setShowEventForm(false);
    };

    const hosting = events.filter(e => e.host.userId === 1);
    const attending = events.filter(e => e.host.userId !== 1);

    return (<>


        <div className='event-display'>
            <div className='event-display-content'>
                <div className='display-item'>
                    <div className="banner-small">
                        <img src="/usercreated.png" alt="Small Banner" className='banner-img' />
                    </div>
                    <div>
                        {hosting.map(e => (
                            <div key={e.eventId} className='lined-li'>
                                <span className='event-list-information'>
                                    {e.title}: {moment(e.start).format('MMMM Do, YYYY')}
                                </span>
                                <span className='event-list-buttons '>
                                    <button className='btn btn-sm btn-list button-60' onClick={handleEventSelect(e)} >View</button>
                                </span>
                            </div>
                        ))}
                        <button className='btn-create' onClick={handleNewEventClick}>New Event</button>
                    </div>

                </div>
                <div className='display-item'>
                    <div className="banner-small">
                        <img src="/useraccepted.png" alt="Small Banner" className='banner-img' />
                    </div>
                    <div>
                        {attending.map(e => (
                            <div key={e.eventId} onClick={handleEventSelect(e)} className='lined-li'>
                                <span className='event-list-information'>
                                {e.title}: {moment(e.start).format('MMMM Do, YYYY')}
                                </span>
                                <span className='event-list-buttons'>
                                    <button className='btn btn-sm btn-list' onClick={handleEventSelect(e)} >View</button>
                                </span>
                            </div>
                        ))}
                    </div>
                </div>
                {selectedEvent && <EventInfo event={selectedEvent} onClose={closePopup} fromInvite={false} />}
                {showEventForm && <EventForm event={defaultEvent} onClose={closeEventForm} />}
            </div>
        </div>

    </>)


}

export default EventList;