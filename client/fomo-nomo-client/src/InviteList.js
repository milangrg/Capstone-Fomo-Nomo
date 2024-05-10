import EventInfo from "./EventInfo";
import moment from 'moment';
import { useState, useEffect } from 'react';

function InviteList() {

  const [invites, setInvites] = useState([])
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [selectedInvite, setSelectedInvite] = useState(null);

  const url = 'http://localhost:8080/api/invitation/invites/1'


  useEffect(() => {
    getInvites();
  }, []);


  const getInvites = () => {
    fetch(url)
      .then(response => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected status code: ${response.status}`);
        }
      })
      .then(data => setInvites(data))
      .catch(console.log)
  }

  const accepted = invites.filter(i => i.status === 'ACCEPTED');
  const pending = invites.filter(i => i.status === 'PENDING');
  const declined = invites.filter(i => i.status === 'DECLINED');


  const closePopup = () => {
    setSelectedEvent(null);
    getInvites();
  };

  const handleSelect = (invite) => {
    setSelectedInvite(invite)
    setSelectedEvent(invite.event)
  }

  return (
    <div className='invite-display'>
      <div className='invite-display-content'>
        <div className='display-item'>
          <div className="banner-small">
            <img src="/pending.png" alt="Small Banner" className='banner-img' />
          </div>
          <div>
            {pending.map(i => (
              <div key={i.invitationId} onClick={() => handleSelect(i)} className='lined-li'><strong>{i.event.title}: {moment(i.event.start).format('MMMM Do, YYYY')}</strong>
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
              <div key={i.invitationId} onClick={() => handleSelect(i)} className='lined-li'>{i.event.title}: {moment(i.event.start).format('MMMM Do, YYYY')}
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
              <div key={i.invitationId} onClick={() => handleSelect(i)} className='lined-li'>{i.event.title}: {moment(i.event.start).format('MMMM Do, YYYY')}
                <span className='invite-list-buttons'>
                  <button className='btn btn-sm btn-list btn-invite'>View</button>
                </span>
              </div>
            ))}
          </div>
          {selectedEvent && <EventInfo event={selectedEvent} onClose={closePopup} fromInvite={true} invite={selectedInvite} />}
        </div>
      </div>
    </div>
  );
}

export default InviteList;
