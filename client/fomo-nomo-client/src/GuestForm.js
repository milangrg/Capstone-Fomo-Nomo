import { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

const GuestForm = ({ event, onClose }) => {

    // console.log(event)

    // const allGuests = [

    //     {
    //         id: 2,
    //         firstName: 'Some',
    //         lastName: 'Guy'
    //     },
    //     {
    //         id: 3,
    //         firstName: 'Bob',
    //         lastName: 'Roberts'
    //     },
    //     {
    //         id: 4,
    //         firstName: 'Someone',
    //         lastName: 'Else'
    //     }

    // ]

    // const invitedGuests = [

    //     {
    //         id: 3,
    //         firstName: 'Bob',
    //         lastName: 'Roberts'
    //     },
    //     {
    //         id: 4,
    //         firstName: 'Someone',
    //         lastName: 'Else'
    //     }

    // ]

    // const theseGuests = allGuests.map(g => ({
    //     ...g,
    //     isInvited: invitedGuests.some(i => i.id === g.id)
    // }));

    // GET ALL USERS 

    // GET ALL INVITED USERS (IF ANY)

    // CHECK AVAILABILITY 

    // DELETE EVENT 

    // SEND GUESTLIST 




    const [allGuests, setAllGuests] = useState([]);
    const [invitedGuests, setInvitedGuests] = useState([]);
    const [inviteList, setInviteList] = useState([]);
    const [errors, setErrors] = useState([]);

    const deleteUrl = 'http://localhost:8080/api/event'
    const conflictUrl = 'http://localhost:8080/api/invitation/conflict/1'
    const allUsersUrl = 'http://localhost:8080/api/user'
    const invitedGuestsUrl = 'http://localhost:8080/api/event/guests'
    const sendInvitesUrl = 'http://localhost:8080/api/invitation/invites/1'

    const defaultInvite = {
        invitationId: 0, 
        event: event,
        guestId: 0, 
        status: 'PENDING'
    }

    // get all guests 
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
            if(guest.userId === id){
                return {...guest, isInvited: !guest.isInvited}
            }
            return guest;
        })
        setAllGuests(updatedGuests);
        console.log(id)
        console.log(updatedGuests)
    };

    const handleCheckAvailability = () => {

    }; 


    const handleSkip = () => {
        generateSuccessMessage();
    }

    const generateSuccessMessage = () => {
        window.alert("Your event was successfully created. :)")
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
        if (finalGuestList.length > 0) {
            generateInviteList(finalGuestList);
            // console.log(finalGuestList)
            // sendGuestList();
        } else {
            generateSuccessMessage();
        }
    }

    const generateInviteList = (finalGuests) => {

        const inviteList = [];
        // console.log(invitedGuests)

        finalGuests.forEach(guest => {
            const invite = {...defaultInvite};
            invite.guestId = guest.userId;
            console.log(`${guest.userId} ${guest.firstName}`)
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
            // .then(data => {
            //     if(data.get(0).invitationId > 0) {
            //         generateSuccessMessage('Invites sent!')                   
            //     } else {
            //         setErrors(data);
            //         // console.log(errors)
            //     }
            // })
            .catch(console.log)
         
    }

    return (
        <>
            <main className='form-container'>
                <div className='form-data'>
                    <h3 className='form-header'>Invite Guests (optional)</h3>
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