import { useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

const GuestForm = ({ event, onClose }) => {

    const allGuests = [

        {
            id: 2,
            firstName: 'Some',
            lastName: 'Guy'
        },
        {
            id: 3,
            firstName: 'Bob',
            lastName: 'Roberts'
        },
        {
            id: 4,
            firstName: 'Someone',
            lastName: 'Else'
        }

    ]

    const invitedGuests = [

        {
            id: 3,
            firstName: 'Bob',
            lastName: 'Roberts'
        },
        {
            id: 4,
            firstName: 'Someone',
            lastName: 'Else'
        }

    ]

    const theseGuests = allGuests.map(g => ({
        ...g,
        isInvited: invitedGuests.some(i => i.id === g.id)
    }));


    const [guests, setGuests] = useState(theseGuests);


    const handleInviteToggle = (id) => {
        const updatedGuests = guests.map(guest =>
            guest.id === id ? { ...guest, isInvited: !guest.isInvited } : guest
        );
        setGuests(updatedGuests);
    };


    const handleSubmit = (e) => {

        e.preventDefault();
        // const finalGuestList = theseGuests.filter(g => g.isInvited);
        // setGuests(finalGuestList);

        // if (id > 0) {
        //     updateEvent();
        // } else {
        //     addAEvent();
        // }
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
                                <label key={guest.id}>
                                    <input
                                        type="checkbox"
                                        checked={guest.isInvited}
                                        onChange={() => handleInviteToggle(guest.id)}
                                    />
                                    {guest.firstName} {guest.lastName}
                                </label>
                            ))}
                        </div>
                    </fieldset>
                    <div className='form-btn-container'>
                        <button className='btn form-btn btn-blue' type='submit'>Submit</button>
                        <button className='btn form-btn' type='button'>Check Availability</button>
                        <button className='btn form-btn' type='button'>Skip</button>
                        <Link className='btn form-btn btn-grey' type='button' onClick={onClose}>Cancel</Link>
                    </div>
                    
                </form>
                </div>
            </main>
        </>
    )

}

export default GuestForm;