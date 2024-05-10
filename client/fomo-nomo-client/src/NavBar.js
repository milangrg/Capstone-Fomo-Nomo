import { Link } from "react-router-dom";

function NavBar() {
    return (<>
        <div className='nav-welcome'>
            <div>
                <nav className='nav-bar justify-content-center'>
                    <Link to={'/'}>HOME</Link>|
                    <Link to={`/invites/`}>INVITES</Link>|
                    <Link to={`/events/`}>EVENTS</Link>
                </nav>
            </div>
            <div className='welcome-div'>
                <h4>Welcome, You Zer</h4>
            </div>
        </div>
    </>)
}

export default NavBar;