import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import { Pagination } from "../Utils/Pagination";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { SearchBook } from "./components/SearchBook";


export const SearchBooksPage = () => {
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
    const [search, setSearch] = useState('');
    const [searchUrl, setSearchUrl] = useState('');
    const [categorySelection, setCategorySelection] = useState('Book Category');

    useEffect(() => {
        //Function defined inside of useEffect that gets called further down.
        //asynch means it will wait for a promise to come back.
        const fetchBooks = async () => {
            const baseUrl: string = "http://localhost:8080/api/books";

            let url: string = '';
            //If searchUrl is an empty string, do the basic book search. If not, then search for the book user is trying to find using search.
            if (searchUrl === '') {
                url = `${baseUrl}?page=${currentPage - 1}&size=${booksPerPage}`;
            } else {
                //We are now dynamically swapping out our placeholder <pageNumber> in the searchUrl with currentPage - 1 so pagination with the categories works.
                var searchWithPage = searchUrl.replace('<pageNumber>', `${currentPage - 1}`)
                url = baseUrl + searchWithPage;
            }

            //The fetch API is promised based (returns a promise). It is better to write code with async and await.
            //Since this is an async function, await is used (instead of .then() which can instead be used without "async". Don't use .then()).
            //Use await to read the the resolved value of the promise and assing the response object to the const variable.
            //create a new response object const var that will get all the response data from spring boot application
            const responseObj = await fetch(url);

            //Guard clause
            //Make sure the request above worked. If the response is not ok, throw an error.
            if (!responseObj.ok) {
                //Something went wrong. Throw an error.
                throw new Error("Something went wrong!");
            }

            //The .json() API returns a promise so we use "await" to resolve the value
            //Get the JSON from the response object.
            const responseJson = await responseObj.json();

            //Get array of book objects from inside "_embedded" object in the JSON.
            const responseData = responseJson._embedded.books;

            //Set state varables to values from an object in the Json response pertaining to pagination...
            setTotalAmountOfBooks(responseJson.page.totalElements);
            setTotalPages(responseJson.page.totalPages);

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

        //Each time useEffect is kicked off, scroll to the top of the page...
        window.scrollTo(0, 0);

        //If this array is empty, this useEffect will not get called again. We only want the initial 9 items from the API. 
        //If we add variables to this array, and those variables change, it kicks off the entire useEffect.
        //Now, each time "currentPage" or "searchUrl" changes, this hook will get recalled.
    }, [currentPage, searchUrl]);

    //If isLoading is true, display "Loading.." in dom
    if (isLoading) {
        return (
            <SpinnerLoading />
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

    //Create a new function: Handle all search changes
    const searchHandleChange = () => {
        setCurrentPage(1);
        if (search === '') {
            setSearchUrl('');
        } else {
            setSearchUrl(`/search/findByTitleContaining?title=${search}&page=<pageNumber>&size=${booksPerPage}`);
            //setSearchUrl(`/search/findByTitleContaining?title=${search}&page=0&size=${booksPerPage}`);
        }
        setCategorySelection('Book category');
    }

    const categoryField = (value: string) => {
        //Here we will be using a generic <pageNumber> as a placeholder within our API endpoint, and always resetting our currentPage state to 1
        setCurrentPage(1);
        if(value.toLowerCase() === 'fe' || value.toLowerCase() === 'be' || value.toLowerCase() === 'data' || value.toLowerCase() === 'devops') {
            setCategorySelection(value);
            setSearchUrl(`/search/findByCategory?category=${value}&page=<pageNumber>&size=${booksPerPage}`);
            //setSearchUrl(`/search/findByCategory?category=${value}&page=0&size=${booksPerPage}`)
        } else {
            setCategorySelection('All');
            setSearchUrl(`?page=<pageNumber>&size=${booksPerPage}`);
            //setSearchUrl(`?page=0&size=${booksPerPage}`);
        }
    }


    const indexOfLastBook: number = currentPage * booksPerPage;
    const indexOfFirstBook: number = indexOfLastBook - booksPerPage;
    let lastItem = booksPerPage * currentPage <= totalAmountOfBooks ? booksPerPage * currentPage : totalAmountOfBooks;

    //Setting up a method assigned to a const variable
    const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

    return (
        <div>
            <div className='container'>
                <div>
                    <div className='row mt-5'>
                        <div className='col-6'>
                            <div className='d-flex'>
                                <input className="form-control me-2" type="search" placeholder="Search" aria-labelledby="Search" onChange={e => setSearch(e.target.value)} />
                                <button className="btn btn-outline-success" onClick={() => searchHandleChange()}>
                                    Search
                                </button>
                            </div>
                        </div>
                        <div className="col-4">
                            <div className="dropdown">
                                <button className="btn btn-secondary dropdown-toggle" type="button"
                                    id="dropdownMenuButton1" data-bs-toggle="dropdown"
                                    aria-expanded="false">
                                    {categorySelection}
                                </button>
                                <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                                    <li onClick={() => categoryField('All')}>
                                        <a className="dropdown-item" href="#">
                                            All
                                        </a>
                                    </li >
                                    <li onClick={() => categoryField('FE')}>
                                        <a className="dropdown-item" href="#">
                                            Front End
                                        </a>
                                    </li>
                                    <li onClick={() => categoryField('BE')}>
                                        <a className="dropdown-item" href="#">
                                            Back End
                                        </a>
                                    </li>
                                    <li onClick={() => categoryField('Data')}>
                                        <a className="dropdown-item" href="#">
                                            Data
                                        </a>
                                    </li>
                                    <li onClick={() => categoryField('DevOps')}>
                                        <a className="dropdown-item" href="#">
                                            DevOps
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    {totalAmountOfBooks > 0 ?
                        <>
                            <div className="mt-3">
                                <h5>Number of results: ({totalAmountOfBooks})</h5>
                            </div>
                            <p>
                                {indexOfFirstBook + 1} to {lastItem} of {totalAmountOfBooks} items:
                            </p>

                            {books.map(book => (
                                <SearchBook book={book} key={book.id} />
                            ))}
                        </>
                        :
                        <div className="m-5">
                            <h3>
                                Can't find what you are looking for?
                            </h3>
                            <a type="button" className="btn main-color btn-md px-4 me-md-2 fw-bold text-white"
                                href="#">Library Services</a>
                        </div>
                    }
                    {totalPages > 1 && <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate} />}

                </div>
            </div>
        </div>
    );

}