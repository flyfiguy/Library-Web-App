import { useOktaAuth } from "@okta/okta-react";
import { useState } from "react";
import { Redirect } from "react-router-dom";
import { AdminMessages } from "./components/AdminMessages";

export const ManageLibraryPage = () => {
    
    const {authState} = useOktaAuth();

    const [changeQuantityOfBooksClick, setChangeQuantityOfBooksClick] = useState(false);
    const [messagesClick, setMessagesClick] = useState(false);

    function addBookClickFunction() {
        setChangeQuantityOfBooksClick(false);
        setMessagesClick(false);
    }

    function changeQuantityOfBooksClickFunction() {
        setChangeQuantityOfBooksClick(true);
        setMessagesClick(false);
    }

    function messagesClickFunction() {
        setChangeQuantityOfBooksClick(false);
        setMessagesClick(true);
    }
    
    //Check "userType" in token to "authorize" user. Reroute home if needs be.
    //Could change logic to check for value "admin" which is what is in the token...
    //otherwise userType does not exist in token so you get undefined (could add a value in token for non admin if wanted).
    if(authState?.accessToken?.claims.userType === undefined) {
        return <Redirect to='/home'/>
    }

    return(
        <div className='container'>
            <div className='mt-5'>
                <h3>Manage Liabrary</h3>
                <nav>
                    <div className="nav nav-tabs" id='nav-tab' role='tablist'>
                        <button onClick={addBookClickFunction} className='nav-link active' id='nav-add-book-tab' data-bs-toggle='tab' 
                            data-bs-target='#nav-add-book' type='button' role='tab' aria-controls='nav-add-book'
                            aria-select='false'
                        >
                            Add New Book
                        </button>
                        <button onClick={changeQuantityOfBooksClickFunction} className='nav-link' id='nav-quantity-tab' data-bs-toggle='tab' 
                            data-bs-target='#nav-quantity' type='button' role='tab' aria-controls='nav-quantity'
                            aria-select='true'
                        >
                            Change Quantity
                        </button>
                        <button onClick={messagesClickFunction} className='nav-link' id='nav-messages-tab' data-bs-toggle='tab' 
                            data-bs-target='#nav-messages' type='button' role='tab' aria-controls='nav-messages'
                            aria-select='false'
                        >
                            Messages
                        </button>
                    </div>
                </nav>
                <div className="tab-content" id='nav-tabContent'>
                    <div className="tab-pane fade show active" id='nav-add-book' role='tabpanel' 
                        aria-labelledby="nav-add-book-tab">
                            Add New Book
                    </div>
                    <div className="tab-pane fade" id='nav-quantity' role='tabpanel' aria-labelledby="nav-quantity-tab">
                        {changeQuantityOfBooksClick ? <>Change Quantity</> : <></>}
                    </div>
                    <div className="tab-pane fade" id='nav-messages' role='tabpanel' aria-labelledby="nav-messages-tab">
                        {messagesClick ? <AdminMessages/> : <></>}
                    </div>
                </div>
            </div>
        </div>
    );
}