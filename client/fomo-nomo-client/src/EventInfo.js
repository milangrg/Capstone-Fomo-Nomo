import moment from 'moment';
import EventForm from './EventForm';
import { useState } from 'react';

const EventInfo = ({ event, onClose, fromInvite }) => {
    const [editMode, setEditMode] = useState(false);

    const handleEdit = () => {
        setEditMode(true);
    };

    const handleDelete = () => {
        if (window.confirm(`Cancel ${event.title}? This action will remove the event for you and any other attendees.`)) {
            //logic to remove
        }
    }

    const handleRSVP = (wasAccepted) => () => {

        if (wasAccepted) {
            console.log('accepted')
        } else {
            console.log('declined')
        }
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
                        <div>
                            {isSameDay ? (
                                <p>
                                    Date: {startDate.format('MMMM Do, YYYY')}<br />
                                    Time: {startDate.format('h:mm a')} - {endDate.format('h:mm a')}
                                </p>
                            ) : (
                                <p>
                                    Start: {startDate.format('MMMM Do, YYYY, h:mm a')}<br />
                                    End: {endDate.format('MMMM Do, YYYY, h:mm a')}
                                </p>
                            )}
                        </div>
                        <div className='info-location'>
                            <div className='info-address'>
                                {event.location.locationName && <div className='location-name'>{event.location.locationName}</div>}
                                <span>{event.location.address}, </span>
                                <span>{event.location.city}, </span>
                                <span>{event.location.state} </span>
                                <span>{event.location.postal}</span>
                            </div>
                        </div>
                        {event.attendees && event.attendees.length > 0 && (
                            <>
                                <h5>Attendees:</h5>
                                <ul>
                                    {event.attendees.map((attendee, index) => (
                                        <li key={index}>{attendee}</li>
                                    ))}
                                </ul>
                            </>
                        )}

                        <div className='info-buttons'>
                            {event.host.userId !== 1 && fromInvite && (
                                <>
                                    <button className='btn btn-blue' onClick={handleRSVP(true)}>Accept</button>
                                    <button className='btn btn-yellow' onClick={handleRSVP(false)}>Decline</button>
                                </>
                            )}
                            {event.host.userId !== 1 && !fromInvite && (
                                <>
                                    <button className='btn btn-yellow' onClick={handleRSVP(false)}>Decline Event</button>
                                </>
                            )}
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