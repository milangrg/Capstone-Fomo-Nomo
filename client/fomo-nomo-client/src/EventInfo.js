import moment from 'moment';
import EventForm from './EventForm';
import { useState, useEffect } from 'react';

const EventInfo = ({ event, onClose, fromInvite, invite = {}}) => {
    
    const [editMode, setEditMode] = useState(false);
    const [eventInvite, setEventInvite] = useState(invite);
    const [errors, setErrors] = useState([]);
    const [guestList, setGuestList] = useState([]);
    const [allUsers, setAllUsers] = useState([]);
    const [guestStatuses, setGuestStatuses] = useState([]);


    const guestListUrl = 'http://localhost:8080/api/invitation/invites/event'
    const putUrl = 'http://localhost:8080/api/invitation'
    const deleteUrl = 'http://localhost:8080/api/event'
    const allUsersUrl = 'http://localhost:8080/api/user'

    // LIST OF INVITEES AND STATUSES (send eventId)

    useEffect(() => {
        fetch(`${guestListUrl}/${event.eventId}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => setGuestList(data))
            // .then(data => console.log(data))
            .catch(console.log)
  
    }, [guestListUrl, event.eventId]);

    useEffect(() => {
        fetch(allUsersUrl)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            // .then(data => setAllGuests(data))
            .then(data => setAllUsers(data))
            .catch(console.log)

    }, [allUsersUrl]);


    useEffect(() => {
        const statuses = [];
        if (allUsers && guestList) {
            const statusIdMap = new Map();
            guestList.forEach(invite => {
                statusIdMap.set(invite.guestId, invite.status);
            })

            // console.log(statusIdMap)
            
            allUsers.forEach(user => {
                if(statusIdMap.get(user.userId)){
                    // console.log('found it')
                    statuses.push({
                        name: `${user.firstName} ${user.lastName}`, 
                        status: statusIdMap.get(user.userId)})
                }
            })
            setGuestStatuses(statuses)
        }

    }, [allUsers, guestList]);


    // GET FOR CONFLICTING EVENTS 



    // console.log(invite)
    

    const handleEdit = () => {
        setEditMode(true);
    };

    const handleDelete = () => {
        if (window.confirm(`Cancel ${event.title}? This action will remove the event for you and any other attendees.`)) {
            const init = {
                method: 'DELETE'
            };
            fetch(`${deleteUrl}/${event.eventId}`, init)
                .then(response => {
                    if (response.status === 204) {
                        window.alert(`Event ${event.eventId} successfully deleted.`)
                        onClose();
                    } else {
                        return Promise.reject(`Unexpected Status Code: ${response.status}`);
                    }
                })
                .catch(console.log)
        }
    }

    const handleRSVP = (wasAccepted) => () => {

        const updatedInvite = {...invite};
        updatedInvite.status = wasAccepted ? 'ACCEPTED' : 'DECLINED'

        updateInvite(updatedInvite);
    }

    const updateInvite = (updatedInvite) => {
        
        console.log(updatedInvite)

        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedInvite)
        };

        fetch(`${putUrl}/${eventInvite.invitationId}`, init)
            .then(response => {
                if (response.status === 204) {
                    return null;
                } else if (response.status === 400) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (!data) {
                    window.alert('Invitation status successfully updated. :)');
                    onClose();
                } else {
                    setErrors(data);
                }
            })
            .catch(console.log);
    }


    const startDate = moment(event.start);
    const endDate = moment(event.end);

    const isSameDay = startDate.isSame(endDate, 'day');


    return (
        <>
            {editMode ? (
                <EventForm event={event} onClose={onClose} />
            ) : (
                <div className='event-info'>
                    <div className='event-data'>
                        <h3>{event.title}</h3>
                        <p>{event.description}</p>
                        <p><strong>Event Creator:</strong> {event.host.firstName} {event.host.lastName}</p>
                        <div>
                            {isSameDay ? (
                                <p>
                                    <strong>Date:</strong> {startDate.format('MMMM Do, YYYY')}<br />
                                    <strong>Time:</strong> {startDate.format('h:mm a')} - {endDate.format('h:mm a')}
                                </p>
                            ) : (
                                <p>
                                    <strong>Start:</strong> {startDate.format('MMMM Do, YYYY, h:mm a')}<br />
                                    <strong>End:</strong> {endDate.format('MMMM Do, YYYY, h:mm a')}
                                </p>
                            )}
                        </div>
                        <p>
                            <strong>Event Type:</strong> {event.eventType}
                        </p>
                        <div className='info-section'>
                            <div className='info-address'>
                            <div><strong>Location:</strong></div>
                                {event.location.locationName && <div className='location-name'>{event.location.locationName}</div>}
                                <span>{event.location.address}, </span>
                                <span>{event.location.city}, </span>
                                <span>{event.location.state} </span>
                                <span>{event.location.postal}</span>
                            </div>
                        </div>
                        {guestStatuses && guestStatuses.length > 0 && (
                            <div className='info-section'>
                                <div><strong>Guest list:</strong></div>
                                <ul>
                                    {guestStatuses.map((guest, i) => (
                                        <li key={i}>{guest.name}: {guest.status}</li>
                                    ))}
                                </ul>
                            </div>
                        )}
                        
                        <div className='info-buttons'>
                            {event.host.userId !== 1 && fromInvite && (
                                <>
                                    <button className='btn btn-blue' onClick={handleRSVP(true)}>Accept</button>
                                    <button className='btn btn-yellow' onClick={handleRSVP(false)}>Decline</button>
                                </>
                            )}
                            {/* {event.host.userId !== 1 && !fromInvite && (
                                <>
                                    <button className='btn btn-yellow' onClick={handleRSVP(false)}>Decline Event</button>
                                </>
                            )} */}
                            {event.host.userId === 1 && (

                                <>
                                    <button className='btn btn-blue' onClick={handleEdit}>Edit Event</button>
                                    <button className='btn btn-yellow' onClick={handleDelete}>Cancel Event</button>
                                </>
                            )}
                            <button className='btn btn-grey' onClick={onClose}>Close</button>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}

export default EventInfo;