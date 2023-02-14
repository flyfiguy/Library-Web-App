import React from 'react';
import './App.css';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { Footer } from './layouts/NavbarAndFooter/Footer'
import { HomePage } from './layouts/HomePage/HomePage';
import { SearchBooksPage } from './layouts/SearchBooksPage/SearchBooksPage';
import { Redirect, Route, Switch } from 'react-router-dom';
import { BookCheckoutPage } from './layouts/BookCheckoutPage/BookCheckoutPage';

export const App = () => {
    //Bootstrap navbar at top of page

    //When upgrading to react router dom 6, there are change that impact route and switch...
    //https://reactrouter.com/en/main/start/faq#what-happened-to-withrouter-i-need-it
    //https://reactrouter.com/en/main/upgrading/v5

    return (
        <div className='d-flex flex-column min-vh-100'>
            <Navbar />

            <div className='flex-grow-1'>

            <Switch>

                <Route path='/' exact>
                    <Redirect to= '/home'/>
                </Route>

                <Route path='/home'>
                    <HomePage />
                </Route>

                <Route path='/search'>
                    <SearchBooksPage />
                </Route>

                <Route path='/checkout/:bookId'>
                    <BookCheckoutPage />
                </Route>
            </Switch>

            </div>

            <Footer />
        </div>
    );
}

