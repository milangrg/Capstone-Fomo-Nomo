import { Link } from "react-router-dom";

function NavBar() {
    return (<>
        <nav className='nav-bar justify-content-center'>
           <Link to={'/'}>HOME</Link>|
           <Link to={`/invites/`}>INVITES</Link>|
           <Link to={`/events/`}>EVENTS</Link>
        </nav>
    </>)
}

export default NavBar;