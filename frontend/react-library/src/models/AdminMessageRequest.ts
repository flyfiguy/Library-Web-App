//Object used in a request to backend which is a response from admin to a question
class AdminMessageRequest {
    id: number;
    response: string;

    constructor(id: number, response: string) {
        this.id = id;
        this.response = response;
    }
}

export default AdminMessageRequest;
