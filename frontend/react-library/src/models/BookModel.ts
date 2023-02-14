//Add in the the variables and attributes of what the backend spring boot application is looking for for a book and what
//the data base is looking for for a book. So when we call the API we can easily convert the response to a TypeScript object
//that we can us in our application.

class BookModel {
    id: number;
    title: string;
    author?: string; //Question mark means optional variable which can be null.
    description?: string;
    copies?: number;
    copiesAvailable?: number;
    category?: string;
    img?: string;

    //Constructor to create objects when we get the data back for the WS.
    constructor(id: number, title: string, author: string, description: string,
        copies: number, copiesAvailable: number, category: string, img: string) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.copies = copies;
        this.copiesAvailable = copiesAvailable;
        this.category = category;
        this.img = img;

    }
}

export default BookModel;