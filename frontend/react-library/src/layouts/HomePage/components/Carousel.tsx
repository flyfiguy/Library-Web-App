import { ReturnBook } from './ReturnBook'
//useState hook: Our state so we can dynamically create state within the application.
//useEffect hook: Able to call some kind of function or API the first time this component is created.
import { useEffect, useState } from "react";
import BookModel from "../../../models/BookModel";
import { SpinnerLoading } from '../../Utils/SpinnerLoading';
import { Link } from 'react-router-dom';

export const Carousel = () => {

	//State - Keep track of books array state. BookModel array or books we create "BookModel[]" of type array "([])"
	const [books, setBooks] = useState<BookModel[]>([]);
	//State - loading sign while books are loading. Start by displaying loading by default and then turn it off when the state changes.
	const [isLoading, setIsLoading] = useState(true);
	//State - If API fails. Start off as null.
	const [httpError, setHttpError] = useState(null);

	//useEffect hook. This indicates we will call a function and all our code will go inside the brackets.
	//This can be triggered more then one time. First it will be triggered at the creation of the component.
	//It will also get called each time something in this array (variables of state or other variables) changes.
	useEffect(() => {
		//Function defined inside of useEffect that gets called further down.
		//asynch means it will wait for a promise to come back.
		const fetchBooks = async () => {
			const baseUrl: string = "http://localhost:8080/api/books";

			const url: string = `${baseUrl}?page=0&size=9`;

			//await is used since this is an asynchronous function
			//create a new variable that will get all the response data from spring boot application
			const response = await fetch(url);

			//Guard clause
			//Make sure the request above worked. If the response is not ok, throw an error.
			if (!response.ok) {
				//Something went wrong. Throw an error.
				throw new Error("Something went wrong!");
			}

			//Get JSON response which is an object called "_embedded".
			//Await is used since it is an async function where we grab our response and turn it into Json
			const responseJson = await response.json();

			//Get array of book objects from inside "_embedded" object in the JSON.
			const responseData = responseJson._embedded.books;

			//Create an array of type BookModel and assing as an empty array
			const loadedBooks: BookModel[] = [];

			//Loop through the JSON array of books
			for (const key in responseData) {
				//Using the "key", access JSON data and push book objects (using the BookModel constructor) into array
				loadedBooks.push({
					id: responseData[key].id,
					title: responseData[key].title,
					author: responseData[key].author,
					description: responseData[key].description,
					copies: responseData[key].copies,
					copiesAvailable: responseData[key].copiesAvailable,
					category: responseData[key].category,
					img: responseData[key].img
				})
			}

			//Set the state for Books and isLoading
			setBooks(loadedBooks);
			setIsLoading(false);
		};
		//Call fetchBooks function (see above). 
		//Because it is async there could be an error.
		//If any errors of type "any" are caught, set isLoading to false and HttpError to whatever error is returned.
		fetchBooks().catch((error: any) => {
			setIsLoading(false);
			setHttpError(error.message)
		})
		//Since this array is empty, this useEffect will not get called again. We only want the initial 9 items from the API. 
		//If we add variables to this array, and those variables change, it kicks off the entire useEffect.
	}, []);

	//If isLoading is true, display "Loading.." in dom
	if (isLoading) {
		return (
			<SpinnerLoading/>
		)
	}

	//If there is an error, display the error in dom that we set above
	if (httpError) {
		return (
			<div className='container m-5'>
				<p>{httpError}</p>
			</div>
		)
	}

	return (
		<div className='container mt-5' style={{ height: 550 }}>
			<div className='homepage-carousel-title'>
				<h3>Find your next "I stayed up too late reading" book here.</h3>

			</div>
			<div id='carouselExampleControls' className='carousel carousel-dark slide mt-5 d-none d-lg-block' data-bs-interval='false'>

				{/* Desktop */}
				<div className='carousel-inner'>
					<div className='carousel-item active'>
						<div className='row d-flex justify-content-center align-items-center'>
							{books.slice(0, 3).map(book => (
								<ReturnBook book={book} key ={book.id} />
							))}
						</div>
					</div>
					<div className='carousel-item'>
						<div className='row d-flex justify-content-center align-items-center'>
							{books.slice(3, 6).map(book=> (
								<ReturnBook book={book} key ={book.id} />
							))}
						</div>
					</div>
					<div className='carousel-item'>
						<div className='row d-flex justify-content-center align-items-center'>
							{books.slice(6, 9).map(book=> (
								<ReturnBook book={book} key ={book.id} />
							))}
						</div>
					</div>
				</div>
				<button className='carousel-control-prev' type='button'
					data-bs-target='#carouselExampleControls' data-bs-slide='prev'>
					<span className='carousel-control-prev-icon' aria-hidden='true'></span>
					<span className='visually-hidden'>Previous</span>
				</button>
				<button className='carousel-control-next' type='button'
					data-bs-target='#carouselExampleControls' data-bs-slide='next'>
					<span className='carousel-control-next-icon' aria-hidden='true'></span>
					<span className='visually-hidden'>Next</span>
				</button>
			</div>

			{/* Mobile */}
			<div className='d-lg-none mt-3'>
				<div className='row d-flex justify-content-center aligh-items-center'>
					<ReturnBook book={books[7]} key={books[7].id}/>
				</div>
			</div>
			<div className='hopepage-carousel-title mt-3'>
				<Link className='btn btn-outline-secondary btn-lg' to='search'>View More</Link>
			</div>
		</div>
	);
}