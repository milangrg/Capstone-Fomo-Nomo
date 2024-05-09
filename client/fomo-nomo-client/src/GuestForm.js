import { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

const GuestForm = ({ event, onClose }) => {


    // GET ALL USERS 

    // GET ALL INVITED USERS (IF ANY)

    // CHECK AVAILABILITY 

    // DELETE EVENT 

    // SEND GUESTLIST 




    const [allGuests, setAllGuests] = useState([]);
    const [invitedGuests, setInvitedGuests] = useState([]);
    const [inviteList, setInviteList] = useState([]);
    const [errors, setErrors] = useState([]);
    const [conflicts, setConflicts] = useState([]);
    const [conflictFree, setConflictFree] = useState(false);

    const deleteUrl = 'http://localhost:8080/api/event'
    const conflictUrl = 'http://localhost:8080/api/invitation/conflict/1'
    const allUsersUrl = 'http://localhost:8080/api/user/guests/1'
    const invitedGuestsUrl = 'http://localhost:8080/api/event/guests'
    const sendInvitesUrl = 'http://localhost:8080/api/invitation/invites/1'

    const defaultInvite = {
        invitationId: 0,
        event: event,
        guestId: 0,
        status: 'PENDING'
    }

    // DON'T WANT HOST 
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
            .then(data => setInviteBools(data))
            .catch(console.log)

    }, []);

    const setInviteBools = (guests) => {
        const guestsPlusInvitedBool = guests.map(guest => ({
            ...guest,
            isInvited: false
        }));
        setAllGuests(guestsPlusInvitedBool);

    }

    const handleInviteToggle = (id) => {
        const updatedGuests = allGuests.map(guest => {
            if (guest.userId === id) {
                return { ...guest, isInvited: !guest.isInvited }
            }
            return guest;
        })
        setAllGuests(updatedGuests);
    };

    const handleCheckAvailability = () => {

        const inviteList = [];

        allGuests.forEach(guest => {
            if (guest.isInvited) {
                const invite = { ...defaultInvite };
                invite.guestId = guest.userId;
                inviteList.push(invite)
            }
        })

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(inviteList)
        };
        fetch(conflictUrl, init)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (data && data.length > 0) {
                    setConflictFree(false);
                    setConflicts(data);
                } else {
                    setConflictFree(true);
                    setConflicts([]);

                }
            })
            .catch(console.log)

    };

    const handleSkip = (msg) => {
        generateSuccessMessage("Your event was successfully created. :)");
    }

    const generateSuccessMessage = (msg) => {
        window.alert(msg)
        onClose();

    }

    const handleDeleteEvent = () => {
        if (window.confirm(`Abandon ${event.title}? All changes will be lost.`)) {
            const init = {
                method: 'DELETE'
            };
            fetch(`${deleteUrl}/${event.eventId}`, init)
                .then(response => {
                    if (response.status === 204) {
                        window.alert(`Event creation cancelled.`)
                        onClose();
                    } else {
                        return Promise.reject(`Unexpected Status Code: ${response.status}`);
                    }
                })
                .catch(console.log)
        }
    }


    const handleSubmit = (e) => {

        e.preventDefault();
        const finalGuestList = allGuests.filter(g => g.isInvited);
        generateInviteList(finalGuestList);

    }

    const generateInviteList = (finalGuestList) => {

        const inviteList = [];

        finalGuestList.forEach(guest => {
            const invite = { ...defaultInvite };
            invite.guestId = guest.userId;
            inviteList.push(invite)
        })

        sendInviteList(inviteList)

    }

    const sendInviteList = (inviteList) => {

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(inviteList)
        };
        fetch(sendInvitesUrl, init)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if(data[0].invitationId > 0) {
                    generateSuccessMessage('Invitations sent!')                   
                } else {
                    setErrors(data);
                    // console.log(errors)
                }
            })
            .catch(console.log)

    }

    return (
        <>
            <main className='form-container'>
                <div className='form-data'>
                    <h3 className='form-header'>Invite Guests (optional)</h3>
                    {conflicts.length > 0 && (
                        <div className='conflict-list'>
                            <h5>Guests with scheduling conflicts: </h5>
                            {conflicts.map(conflict => (
                                <p key={conflict.userId}>-{conflict.firstName} {conflict.lastName}</p>
                            ))}

                        </div>
                    )}
                    {conflictFree && (
                        <div className='non-conflict'>
                            <h5>No conflicts found. Nice! </h5>
                        </div>
                    )}
                    <form onSubmit={handleSubmit}>
                        <fieldset>
                            <div className='form-guests'>
                                {allGuests.map(guest => (
                                    <label key={guest.userId}>
                                        <input
                                            type="checkbox"
                                            checked={guest.isInvited}
                                            onChange={() => handleInviteToggle(guest.userId)}
                                        />
                                        {guest.firstName} {guest.lastName}
                                    </label>
                                ))}
                            </div>
                        </fieldset>
                        <div className='form-btn-container'>
                            <button className='btn form-btn btn-blue' type='submit'>Submit</button>
                            <button className='btn form-btn' type='button' onClick={handleCheckAvailability}>Check Availability</button>
                            <button className='btn form-btn' type='button' onClick={handleSkip}>Skip</button>
                            <Link className='btn form-btn btn-grey' type='button' onClick={handleDeleteEvent}>Cancel Event Creation</Link>
                        </div>

                    </form>
                </div>
            </main>
        </>
    )

}

export default GuestForm;