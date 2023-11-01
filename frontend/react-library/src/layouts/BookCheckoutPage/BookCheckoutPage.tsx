
import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import ReviewModel from "../../models/ReviewModel";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { StarsReview } from "../Utils/StarsReview";
import { CheckoutAndReviewBox } from "./CheckoutAndReviewBox";
import { LatestReviews } from "./LatestReviews";
import { useOktaAuth} from "@okta/okta-react";
import ReviewRequestModel from "../../models/ReviewRequestModel";

export const BookCheckoutPage = () => {
    //Add state

    //Okta
    const {authState} = useOktaAuth();

    //Book to display on the page
    const [book, setBook] = useState<BookModel>();
    //For a loading screen
    const [isLoading, setIsLoading] = useState(true);
    //In case there is a problem with the API calls
    const [httpError, setHttpError] = useState(null);
    
    //Review State
    const[reviews, setReviews] = useState<ReviewModel[]>([])
    const[totalStars, setTotalStars] = useState(0);
    const[isLoadingReview, setIsLoadingReview] = useState(true);
    
    const[isReviewLeft, setIsReviewLeft] = useState(false);
    const[isLoadingUserReview, setIsLoadingUserReview] = useState(true);

    //Loans Count State
    const[currentLoansCount, setCurrentLoansCount] = useState(0);
    const[isLoadingCurrentLoansCount, setIsLoadingCurrentLoansCount] = useState(true);

    //Is Book checked out
    const[isCheckedOut, setIsCheckedOut] = useState(false);
    const[isLoadingBookCheckedOut, setIsLoadingBookCheckedOut] = useState(true);


    //Will return the URL: window.location.pathname
    //Split the URL into an array using the slash as a token. We need the last element which is the book id query param
    //localhost:3000/checkout/<bookId>
    const bookId = (window.location.pathname).split('/')[2];

    useEffect(() => {
        //Function defined inside of useEffect that gets called further down.
        //asynch means it will wait for a promise to come back.
        //The back tic allows us to dynamically pass in variables.
        const fetchBook = async () => {
            const baseUrl: string = `${process.env.REACT_APP_API}/books/${bookId}`;


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

            //Create BookModel from response values
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
    }, [isCheckedOut]);

    useEffect(()=>{
        const fetchBookReviews = async () => {
            const reviewUrl: string = `${process.env.REACT_APP_API}/reviews/search/findByBookId?bookId=${bookId}`;

            const responseReviews = await fetch(reviewUrl);

            if(!responseReviews.ok){
                throw new Error('Something went wrong!');
            }

            const responseJsonReviews = await responseReviews.json();
            const responseData = responseJsonReviews._embedded.reviews;
            const loadedReviews: ReviewModel[] = [];
            let weightStarReviews: number = 0;
            for(const key in responseData) {
                loadedReviews.push({
                    id: responseData[key].id,
                    userEmail: responseData[key].userEmail,
                    date: responseData[key].date,
                    rating: responseData[key].rating,
                    book_id: responseData[key].bookId,
                    reviewDescription: responseData[key].reviewDescription
                });
                weightStarReviews = weightStarReviews + responseData[key].rating;
            }

            if(loadedReviews) {
                const round = (Math.round((weightStarReviews / loadedReviews.length) * 2) / 2).toFixed(1);
                setTotalStars(Number(round));
            }

            setReviews(loadedReviews);
            setIsLoadingReview(false);
        };

        fetchBookReviews().catch((error: any) => {
            setIsLoadingReview(false);
            setHttpError(error.message);
        });

    }, [isReviewLeft]);

    useEffect(() => {
        const fetchUserReviewBook = async () => {
            if(authState && authState.isAuthenticated) {
                const url = `${process.env.REACT_APP_API}/reviews/secure/user/book?bookId=${bookId}`;
                const requestOptions = {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                        'Content-Type' : 'application/json'
                    }
                };
                const userReview = await fetch(url, requestOptions);
                if(!userReview.ok) {
                    throw new Error('Something went wrong with fetch user review book');
                }
                const userReviewResponseJson = await userReview.json();
                setIsReviewLeft(userReviewResponseJson);
            }
            setIsLoadingUserReview(false);
        }
        fetchUserReviewBook().catch((error: any) => {
            setIsLoadingUserReview(false);
            setHttpError(error.message);
        });
    }, [authState]);

    useEffect(() => {
        const fetchUserCurrentLoansCount = async () => {
            //Check to see if authState is not null and then check to make sure useer is authenticated.
            if(authState && authState.isAuthenticated) {
                const url=`${process.env.REACT_APP_API}/books/secure/currentloans/count`;
                //Header for authentication in request
                const requestOptions = {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${authState.accessToken?.accessToken}`, //JWT token
                        'Content-Type': 'application/json'
                    }
                };
                const currentLoansCountResponse = await fetch(url, requestOptions);
                if(!currentLoansCountResponse.ok) {
                    throw new Error("Something went wrong with fetching current loans count!");
                }
                const currentLoansCountResponseJson = await currentLoansCountResponse.json();
                setCurrentLoansCount(currentLoansCountResponseJson);
            }
            setIsLoadingCurrentLoansCount(false);
        }

        fetchUserCurrentLoansCount().catch((error: any) => {
            setIsLoadingCurrentLoansCount(false);
            setHttpError(error.message);
        })

    }, [authState, isCheckedOut]);

    useEffect(() => {
        const fetchUserCheckedOutBook = async () => {
            if(authState && authState.isAuthenticated) {
                const url = `${process.env.REACT_APP_API}/books/secure/ischeckedout/byuser?bookId=${bookId}`;
                const requestOptions = {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                        'Conent-Type': 'application/json'
                    }
                };
                const bookCheckedOut = await fetch(url, requestOptions);

                if(!bookCheckedOut.ok) {
                    throw new Error('Something went wrong in fetch for is checked out by user!');
                }

                const bookCheckedOutResponseJson = await bookCheckedOut.json();
                setIsCheckedOut(bookCheckedOutResponseJson);
            }
            setIsLoadingBookCheckedOut(false);
        }

        fetchUserCheckedOutBook().catch((error: any) => {
            setIsLoadingBookCheckedOut(false);
            setHttpError(error.message);
        }) 

    }, [authState]);

    //If isLoading is true, display "Loading.." in dom
    //The || in the if statement makes sure that all useEffect API calls are complete before we go ahead and render the remainder of the application
    if (isLoading || isLoadingReview || isLoadingCurrentLoansCount || isLoadingBookCheckedOut || isLoadingUserReview) {
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

    async function checkoutBook() {
        const url=`${process.env.REACT_APP_API}/books/secure/checkout?bookId=${book?.id}`;
        const requestOptions = {
            method: 'PUT',
            headers: {
                Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                'Content-Type': 'application/json'
            }
        };
        const checkoutResponse = await fetch(url, requestOptions);
        if(!checkoutResponse.ok) {
            throw new Error("Something went wrong in fetch for BookCheckoutPage.checkoutBook");
        }
        setIsCheckedOut(true);
    }

    async function submitReview(starInput: number, reviewDescription: string) {
        let bookId: number = 0;

        //Helps us to know that there is in fact a bookId
        if(book?.id) {
            bookId = book.id;
        }

        const reviewRequestModel = new ReviewRequestModel(starInput, bookId, reviewDescription);
        const url = `${process.env.REACT_APP_API}/reviews/secure`;
        const requestOptions = {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reviewRequestModel)
        };
        const returnResponse = await fetch(url, requestOptions);
        if(!returnResponse.ok) {
            throw new Error('Something went wrong with fetch for submit review!');
        }
        setIsReviewLeft(true);
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
                            <StarsReview rating={totalStars} size={32} />
                        </div>
                    </div>
                    <CheckoutAndReviewBox book={book} mobile={false} currentLoansCount={currentLoansCount} 
                        isAuthenticated={authState?.isAuthenticated} isCheckedOut={isCheckedOut}
                        checkoutBook={checkoutBook} isReviewLeft={isReviewLeft} submitReview={submitReview}/>
                </div>
                <hr />
                <LatestReviews reviews={reviews} bookId={book?.id} mobile={false}/>
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
                        <StarsReview rating={totalStars} size={32} />
                    </div>
                </div>
                <CheckoutAndReviewBox book={book} mobile={true} currentLoansCount={currentLoansCount} 
                    isAuthenticated={authState?.isAuthenticated} isCheckedOut={isCheckedOut}
                    checkoutBook={checkoutBook}  isReviewLeft={isReviewLeft} submitReview={submitReview}/>
                <hr />
                <LatestReviews reviews={reviews} bookId={book?.id} mobile={true}/>
            </div>
        </div>
    );
}