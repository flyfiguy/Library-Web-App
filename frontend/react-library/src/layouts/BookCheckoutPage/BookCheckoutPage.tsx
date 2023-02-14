import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { StarsReview } from "../Utils/StarsReview";
import { CheckoutAndReviewBox } from "./CheckoutAndReviewBox";

export const BookCheckoutPage = () => {
    //Add state

    //Book to display on the page
    const [book, setBook] = useState<BookModel>();
    //For a loading screen
    const [isLoading, setIsLoading] = useState(true);
    //In case there is a problem with the API calls
    const [httpError, setHttpError] = useState(null);

    //Will return the URL: window.location.pathname
    //Split the URL into an array using the slash as a token. We need the last element which is the book id query param
    //localhost:3000/checkout/<bookId>
    const bookId = (window.location.pathname).split('/')[2];

    useEffect(() => {
        //Function defined inside of useEffect that gets called further down.
        //asynch means it will wait for a promise to come back.
        //The back tic allows us to dynamically pass in variables.
        const fetchBook = async () => {
            const baseUrl: string = `http://localhost:8080/api/books/${bookId}`;


            //await is used since this is an asynchronous function
            //create a new variable that will get all the response data from spring boot application
            const response = await fetch(baseUrl);

            //Guard clause
            //Make sure the request above worked. If the response is not ok, throw an error.
            if (!response.ok) {
                //Something went wrong. Throw an error.
                throw new Error("Something went wrong!");
            }

            //Get JSON response which is an object called "_embedded".
            //Await is used since it is an async function where we grab our response and turn it into Json
            const responseJson = await response.json();

            //Create an array of type BookModel and assing as an empty array
            const loadedBook: BookModel = {
                id: responseJson.id,
                title: responseJson.title,
                author: responseJson.author,
                description: responseJson.description,
                copies: responseJson.copies,
                copiesAvailable: responseJson.copiesAvailable,
                category: responseJson.category,
                img: responseJson.img
            };

            //Set the state for Books and isLoading
            setBook(loadedBook);
            setIsLoading(false);
        };
        //Call fetchBooks function (see above). 
        //Because it is async there could be an error.
        //If any errors of type "any" are caught, set isLoading to false and HttpError to whatever error is returned.
        fetchBook().catch((error: any) => {
            setIsLoading(false);
            setHttpError(error.message)
        })
        //Since this array is empty, this useEffect will not get called again. We only want the initial 9 items from the API. 
        //If we add variables to this array, and those variables change, it kicks off the entire useEffect.
    }, []);

    //If isLoading is true, display "Loading.." in dom
    if (isLoading) {
        return (
            <SpinnerLoading />
        )
    }

    //Error Handling: If there is an error, display the error in dom that we set above
    if (httpError) {
        return (
            <div className='container m-5'>
                <p>{httpError}</p>
            </div>
        )
    }

    //Book checkout DOM
    return (
        <div>
            <div className="container d-none d-lg-block">
                <div className="row mt-5 ">
                    <div className="col-sm-2 col-md-2">
                        {book?.img ?
                            <img src={book?.img} width="226" height="349" alt="Book" />
                            :
                            <img src={require("./../../Images/BooksImages/book-luv2code-1000.png")} width="226" height="349" alt="Book" />
                        }
                    </div>
                    <div className="col-4 col-md-4 container">
                        <div className="ml-2">
                            <h2>{book?.title}</h2>
                            <h5 className="text-primary">{book?.author}</h5>
                            <p className="lead">{book?.description}</p>
                            <StarsReview rating={4.5} size={32} />
                        </div>
                    </div>
                    <CheckoutAndReviewBox book={book} mobile={false} />
                </div>
                <hr />
            </div>
            <div className="container d-lg-none mt-5">
                <div className="d-flex justify-content-center align-items-center">
                    {book?.img ?
                        <img src={book?.img} width="226" height="349" alt="Book" />
                        :
                        <img src={require("./../../Images/BooksImages/book-luv2code-1000.png")} width="226" height="349" alt="Book" />
                    }
                </div>
                <div className="mt-4">
                    <div className="ml-2">
                        <h2>{book?.title}</h2>
                        <h5 className="text-primary">{book?.author}</h5>
                        <p className="lead">{book?.description}</p>
                        <StarsReview rating={4.5} size={32} />
                    </div>
                </div>
                <CheckoutAndReviewBox book={book} mobile={true} />
                <hr />
            </div>
        </div>
    );
}