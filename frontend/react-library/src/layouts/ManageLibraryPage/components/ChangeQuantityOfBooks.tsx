import { useEffect, useState } from "react";
import BookModel from "../../../models/BookModel";
import { Pagination } from "../../Utils/Pagination";
import { SpinnerLoading } from "../../Utils/SpinnerLoading";

export const ChangeQuantityOfBooks = () => {
        //State - Keep track of books array state. BookModel array or books we create "BookModel[]" of type array "([])"
        const [books, setBooks] = useState<BookModel[]>([]);
        //State - loading sign while books are loading. Start by displaying loading by default and then turn it off when the state changes.
        const [isLoading, setIsLoading] = useState(true);
        //State - If API fails. Start off as null.
        const [httpError, setHttpError] = useState(null);
    
        const [currentPage, setCurrentPage] = useState(1);
        const [booksPerPage] = useState(5);
        const [totalAmountOfBooks, setTotalAmountOfBooks] = useState(0);
        const [totalPages, setTotalPages] = useState(0);
    
        useEffect(() => {
            //Function defined inside of useEffect that gets called further down.
            //async means it will wait for a promise to come back.
            const fetchBooks = async () => {
                const baseUrl: string = `http://localhost:8080/api/books?page=${currentPage - 1}&size=${booksPerPage}`;
        
                //The fetch API is promised based (returns a promise). It is better to write code with async and await.
                //Since this is an async function, await is used (instead of .then() which can instead be used without "async". Don't use .then()).
                //Use await to read the the resolved value of the promise and assing the response object to the const variable.
                //Synchronous tasks happen in order — you must complete the current task before moving on to the next. Asynchronous tasks are executed in any order or even at once
                //create a new response object const var that will get all the response data from spring boot application
                const responseObj = await fetch(baseUrl);
    
                //Guard clause
                //Make sure the request above worked. If the response is not ok, throw an error.
                if (!responseObj.ok) {
                    //Something went wrong. Throw an error.
                    throw new Error("Something went wrong!");
                }
    
                //If you get to this point, an error was not thrown.
                //The .json() API returns a promise so we use "await" to resolve the value
                //Get the JSON from the response object.
                const responseJson = await responseObj.json();
    
                //Get array of book objects from inside "_embedded" object in the JSON.
                const responseData = responseJson._embedded.books;
    
                //Set state varables to values from an object in the Json response pertaining to pagination...
                setTotalAmountOfBooks(responseJson.page.totalElements);
                setTotalPages(responseJson.page.totalPages);
    
                //Create an array of type BookModel and assign as an empty array
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
        
            //If this array is empty, this useEffect will not get called again. We only want the initial 9 items from the API. 
            //If we add variables to this array, and those variables change, it kicks off the entire useEffect.
            //Now, each time "currentPage" changes, this hook will get recalled which is what we want to re-render that part of the page.
        }, [currentPage]);      
        
        const indexOfLastBook: number = currentPage * booksPerPage;
        const indexOfFirstBook: number = indexOfLastBook - booksPerPage;
        let lastItem = booksPerPage * currentPage <= totalAmountOfBooks ? booksPerPage * currentPage : totalAmountOfBooks;
    
        //Setting up a method assigned to a const variable
        const paginate = (pageNumber: number) => setCurrentPage(pageNumber);     
        
        if(isLoading) {
            return (
                <SpinnerLoading/>
            );
        }
    
        if(httpError) {
            return (
                <div className='container m-5'>
                    <p>{httpError}</p>      
                </div>
            );
        }

    return (
        <div className="container mt-5">
            {totalAmountOfBooks > 0 ?
                <>
                    <div className=" mt-3">
                        <h3>Number of results: ({totalAmountOfBooks})</h3>
                    </div>
                    <p>
                        {indexOfFirstBook + 1} to {lastItem} of {totalAmountOfBooks} items:
                    </p>
                    {books.map(book => (
                        <p>Display different quantity of books here</p>
                    ))}
                </>
                :
                <h5>Add a book before changing quantity</h5>
            }
            {totalPages > 1 && <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate}/>}
        </div>
    );
}