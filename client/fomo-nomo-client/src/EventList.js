import EventInfo from "./EventInfo";
import EventForm from "./EventForm";
import moment from 'moment';
import { useState, useEffect } from 'react';


const defaultEvent = {
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
    "start": new Date(),
    "end": new Date()
}



function EventList() {


    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [showEventForm, setShowEventForm] = useState(false);
    const [hostingEvents, setHostingEvents] = useState([]);
    const [attendingEvents, setAttendingEvents] = useState([]);

    const url = 'http://localhost:8080/api/invitation/1'


    const fetchEvents = () => {
        fetch(url)
            .then(response => response.json())
            .then(data => {
                setEvents(data);
                separateEvents(data);
            })
            .catch(console.error);
    };

    useEffect(() => {
        fetchEvents();
    }, []);

    const separateEvents = (events) => {
        const hosting = events.filter(e => e.host.userId === 1);
        const attending = events.filter(e => e.host.userId !== 1);
        setHostingEvents(hosting);
        setAttendingEvents(attending);
    };

    const handleEventSelect = event => () => {
        setSelectedEvent(event);
    };

    const handleNewEventClick = () => {
        setShowEventForm(true);
    };

    const closePopup = () => {
        setSelectedEvent(null);
        fetchEvents(); 
    };

    const closeEventForm = () => {
        setShowEventForm(false);
        fetchEvents(); 
    };

    return (<>

        <div className='event-display'>
            <div className='event-display-content'>
                <div className='display-item'>
                    <div className="banner-small">
                        <img src="/usercreated.png" alt="Small Banner" className='banner-img' />
                    </div>
                    <div>
                        {hostingEvents.map(e => (
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
                        {attendingEvents.map(e => (
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