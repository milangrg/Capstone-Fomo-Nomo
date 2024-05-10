import EventCalendar from './EventCalendar';


function Home({holidayCalendar}) {


    return (
        <>
            <section>
                <EventCalendar holidayCalendar={holidayCalendar} />
            </section>
        </>
    )
}

export default Home;