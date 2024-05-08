import EventInfo from "./EventInfo";
import { useState } from 'react';

// const url = /api/invitation
// /invites/1(userId)

const invites = [
  {
    "invitationId": 2,
    "event": {
      "eventId": 5,
      "host": {
        "userId": 9,
        "firstName": "Viola",
        "lastName": "Reynolds",
        "email": "vr11_@yahoo2.com",
        "phone": "555-555-9999",
        "dob": "1989-05-19"
      },
      "title": "Golf Outing",
      "description": "It is time for our annual golf tourament!",
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
    "guestId": 1,
    "status": "PENDING"
  },
  {
    "invitationId": 3,
    "event": {
      "eventId": 5,
      "host": {
        "userId": 9,
        "firstName": "Viola",
        "lastName": "Reynolds",
        "email": "vr11_@yahoo2.com",
        "phone": "555-555-9999",
        "dob": "1989-05-19"
      },
      "title": "Big Party",
      "description": "It is time for a big party!",
      "location": {
        "locationId": 7,
        "address": "1550 Golf Path",
        "state": "NY",
        "city": "Saratoga Springs",
        "postal": "12866",
        "locationName": "The Party Emporium"
      },
      "eventType": "SOCIAL",
      "start": "2024-05-13T01:00:00",
      "end": "2024-05-13T05:00:00"
    },
    "guestId":1,
    "status": "ACCEPTED"
  },

  {
    "invitationId": 7,
    "event": {
      "eventId": 3,
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
    "guestId": 1,
    "status": "DECLINED"
  }
]
// need to pull actual invite list and attached events (including guest list)
// need functions to change statuses of invite 

function InviteList() {
  const accepted = invites.filter(i => i.status === 'ACCEPTED');
  const pending = invites.filter(i => i.status === 'PENDING');
  const declined = invites.filter(i => i.status === 'DECLINED');

  const [selectedEvent, setSelectedEvent] = useState(null);

  const handleEventSelect = event => {
    setSelectedEvent(event);
  };

  const closePopup = () => {
    setSelectedEvent(null);
  };

  return (
    <div className='invite-display'>
      <div className='invite-display-content'>
        <div className='display-item'>
          <div className="banner-small">
            <img src="/pending.png" alt="Small Banner" className='banner-img' />
          </div>
          <div>
            {pending.map(i => (
              <div key={i.invitationId} onClick={() => handleEventSelect(i.event)} className='lined-li pending'>{i.event.title}
                <span className='invite-list-buttons'>
                  <button className='btn btn-sm btn-list btn-invite'>View</button>

                </span>
              </div>
            ))}
          </div>
        </div>

        <div className='display-item'>
          <div className="banner-small">
            <img src="/accepted.png" alt="Small Banner" className='banner-img' />
          </div>
          <div>
            {accepted.map(i => (
              <div key={i.invitationId} onClick={() => handleEventSelect(i.event)} className='lined-li'>{i.event.title}
                <span className='invite-list-buttons'>
                  <button className='btn btn-sm btn-list btn-invite'>View</button>

                </span></div>
            ))}
          </div>
        </div>


        <div className='display-item'>
          <div className="banner-small">
            <img src="/declined.png" alt="Small Banner" className='banner-img' />
          </div>
          <div>
            {declined.map(i => (
              <div key={i.invitationId} onClick={() => handleEventSelect(i.event)} className='lined-li'>{i.event.title}
                <span className='invite-list-buttons'>
                  <button className='btn btn-sm btn-list btn-invite'>View</button>
                </span>
              </div>
            ))}
          </div>
          {selectedEvent && <EventInfo event={selectedEvent} onClose={closePopup} fromInvite={true} />}
        </div>
      </div>
    </div>
  );
}

export default InviteList;
