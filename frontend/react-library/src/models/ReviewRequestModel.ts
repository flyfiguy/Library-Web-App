class ReviewRequestModel {
    rating: number;
    bookId: number;
    reviewDescription?: string; //Question mark means this value is optional

    constructor(rating: number, bookId: number, reviewDescription: string) {
        this.rating = rating;
        this.bookId = bookId;
        this.reviewDescription = reviewDescription;

    }
}

export default ReviewRequestModel; //Export it so other files can use it