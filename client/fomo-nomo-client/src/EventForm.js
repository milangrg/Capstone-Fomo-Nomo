import moment from 'moment';
import { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import GuestForm from './GuestForm';

const EventForm = ({ event: originalEvent, onClose }) => {

    const [event, setEvent] = useState(originalEvent);
    const [errors, setErrors] = useState([]);
    // const [startTime, setStartTime] = useState();
    const [startDate, setStartDate] = useState();
    // const [endTime, setEndTime] = useState();
    const [endDate, setEndDate] = useState();
    const [guestFormMode, setGuestFormMode] = useState(false);
    const navigate = useNavigate();

    const postUrl = 'http://localhost:8080/api/event/1';
    const putUrl = 'http://localhost:8080/api/event'
    const { id } = useParams();
    // const conflictUrl = 'http://localhost:8080/api/invitation/conflict/1'


    useEffect(() => {
        if (event) {
            const formattedStartDate = moment(event.start).format('YYYY-MM-DDTHH:mm');
            const formattedEndDate = moment(event.end).format('YYYY-MM-DDTHH:mm');
            setStartDate(formattedStartDate);
            // setStartTime(formattedStartDate);
            setEndDate(formattedEndDate);
            // setEndTime(formattedEndDate);
            // console.log(`start: ${formattedStartDate}`)
            // console.log(`end: ${formattedEndDate}`)
        }
    }, [event]);


    //need function to add event, cancel event, and update event
    //create event must send out invites if they exist as well 
    //add guest functionalities 

    // const url = 'http://localhost:8080/api/event';
    // const navigate = useNavigate();
    // const { id } = useParams();


    let headerText = event.eventId ? 'Update Event' : 'Add Event';


    const handleChange = (e) => {
        const { name, value } = e.target;
        setEvent(prev => ({
            ...prev,
            [name]: value
        }));
        
    };

    const handleStartChange = (e) => {
        const newDate = e.target.value;
        setEvent(prev => ({
            ...prev, 
            start: newDate
        }))

        setStartDate(newDate)

    }

    const handleEndChange = (e) => {
        const newDate = e.target.value;
        setEvent(prev => ({
            ...prev, 
            end: newDate
        }))

        setEndDate(newDate)

    }

    const handleLocationChange = (e) => {
        const { name, value } = e.target;
        setEvent(prev => ({
            ...prev,
            location: {
                ...prev.location,
                [name]: value
            }
        }));
    };
    const handleEventTypeChange = (e) => {
        const newEvent = { ...event };
        newEvent.eventType = e.target.id;
        setEvent(newEvent)
    }


    // const handleGuestClick = () => {
    //     if(addEventThenGuests){
    //         setGuestFormMode(true)
    //     } 
    // }

    // const handleGuestSubmit = () => {
    //     setGuestFormMode(true)

    // }

    const closeGuestForm = () => {
        setGuestFormMode(false)
    }

    const handleSubmit = (e) => {

        e.preventDefault();
        // console.log(event)
        if (event.eventId > 0) {
            updateEvent();
        } else {
            addEvent();
        }
    }

    // const hasConflicts = () =>{
        
    //     const init = {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json'
    //         },
    //         body: JSON.stringify(event)
    //     };
    //     fetch(conflictUrl, init)
    //         .then(response => {
    //             if (response.status === 201 || response.status === 400) {
    //                 return response.json();
    //             } else {
    //                 return Promise.reject(`Unexpected status code: ${response.status}`);
    //             }
    //         })
    //         .then(data => {
    //             if (data.agentId) {
    //                 navigate('/')
    //             } else {
    //                 setErrors(data);
    //             }
    //         })
    //         .catch(console.log)

    // }

    // need add to return event ID (event created, then guest list)

    const addEvent = () => {

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        };
        fetch(postUrl, init)
            .then(response => {
                if (response.status === 201 || response.status === 400) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (data.eventId) {
                    console.log(data.eventId)
                    setEvent(data)
                    setGuestFormMode(true)
                    // return true;                    
                } else {
                    setErrors(data);
                    // console.log(errors)
                }
            })
            .catch(console.log)
    }

    const updateEvent = () => {

        // event.eventId = id;

        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        };

        fetch(`${putUrl}/${event.eventId}`, init)
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
                    window.alert('Event successsfully updated. :)');
                    onClose();
                } else {
                    setErrors(data);
                }
            })
            .catch(console.log);
    }


    return (
        <>
            {guestFormMode ? (
                <GuestForm event={event} onClose={onClose} />
            ) : (
                <main className='form-container overflow-auto'>
                    <div className='form-data'>
                        <h1 className='form-header'>{headerText}</h1>
                        {errors.length > 0 && (
                            <div className='alert error-display'>
                                <span>The following errors were found:</span>
                                <ul>
                                    {errors.map(error => (
                                        <li key={error}>{error}</li>
                                    ))}
                                </ul>
                            </div>
                        )}
                        <form onSubmit={handleSubmit}>
                            <fieldset>
                                <label htmlFor='title'>Title</label>
                                <input
                                    id='title'
                                    name='title'
                                    type='text'
                                    className='form form-control'
                                    value={event.title}
                                    onChange={handleChange}
                                />
                            </fieldset>
                            <fieldset>
                                <label htmlFor='description'>Description</label>
                                <textarea
                                    id='description'
                                    name='description'
                                    className='form form-control'
                                    value={event.description}
                                    onChange={handleChange}
                                />
                            </fieldset>
                            
                            <fieldset>
                                <label htmlFor='start'>Start Date and Time</label>
                                <input
                                    id='start'
                                    name='start'
                                    type='datetime-local'
                                    className='form-control'
                                    value={startDate}
                                    onChange={handleStartChange}
                                />
                            </fieldset>
                            <fieldset>
                                <label htmlFor='end'>End Date and Time</label>
                                <input
                                    id='end'
                                    name='end'
                                    type='datetime-local'
                                    className='form-control'
                                    value={endDate}
                                    onChange={handleEndChange}
                                />
                            </fieldset>

                            <fieldset>
                                <label htmlFor='locationName'>Location Name (Optional)</label>
                                <input
                                    id='locationName'
                                    name='locationName'
                                    type='text'
                                    className='form form-control'
                                    value={event.location.locationName}
                                    onChange={handleLocationChange}
                                />
                            </fieldset>
                            <fieldset>
                                <label htmlFor='address'>Address</label>
                                <input
                                    id='address'
                                    name='address'
                                    type='text'
                                    className='form form-control'
                                    value={event.location.address}
                                    onChange={handleLocationChange}
                                />
                            </fieldset>
                            <fieldset>
                                <label htmlFor='city'>City</label>
                                <input
                                    id='city'
                                    name='city'
                                    type='text'
                                    className='form form-control'
                                    value={event.location.city}
                                    onChange={handleLocationChange}
                                />
                            </fieldset>
                            <fieldset>
                                <label htmlFor='state'>State</label>
                                <input
                                    id='state'
                                    name='state'
                                    type='text'
                                    className='form form-control'
                                    value={event.location.state}
                                    onChange={handleLocationChange}
                                />
                            </fieldset>
                            <fieldset>
                                <label htmlFor='postal'>Postal Code</label>
                                <input
                                    id='postal'
                                    name='postal'
                                    type='text'
                                    className='form form-control'
                                    value={event.location.postal}
                                    onChange={handleLocationChange}
                                />
                            </fieldset>
                            <fieldset>
                                <legend>Event Type</legend>

                                <input type="radio" id="SOCIAL" name="eventType" value={event.eventType} onChange={handleEventTypeChange} />
                                <label htmlFor="social">Social</label><br />

                                <input type="radio" id="APPOINTMENT" name="eventType" value={event.eventType} onChange={handleEventTypeChange} />
                                <label htmlFor="appointment">Appointment</label><br />

                                <input type="radio" id="WORK" name="eventType" value={event.eventType} onChange={handleEventTypeChange} />
                                <label htmlFor="work">Work</label><br />
                                <input type="radio" id="PERSONAL" name="eventType" value={event.eventType}  onChange={handleEventTypeChange} />
                                <label htmlFor="personal">Personal</label><br />
                            </fieldset>
                            <div className='form-btn-container'>
                                {/* creates event */}
                                <button className='btn form-btn btn-blue' type='submit'>Submit/Add Guests</button>
                                {/* creates event and returns ID/routes to guestlist  */}
                                {/* <button className='btn form-btn btn-blue' type='submit' onClick={handleGuestClick}>Guests</button> */}
                                {/* cancels everything */}
                                <Link className='btn form-btn btn-grey' type='button' onClick={onClose}>Cancel</Link>
                            </div>
                        </form>
                    </div>
                    {guestFormMode && <GuestForm event={event} onClose={closeGuestForm} />}
                </main>
            )}

        </>
    )
}

export default EventForm;