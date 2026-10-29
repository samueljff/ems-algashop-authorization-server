package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/users") {
            queryParameters {
                parameter("page", 0)
                parameter("size", 10)
            }
        }
    }
    response {
        status 200
        headers {
            contentType "application/json"
        }
        body([
            size         : 10,
            number       : 0,
            totalElements: 2,
            totalPages   : 1,
            content      : [
                [
                    id     : "01234567-89ab-cdef-0123-456789abcdef",
                    name   : "John Doe",
                    email  : "john.doe@email.com",
                    type   : "MANAGER",
                    enabled: true
                ],
                [
                    id     : "11234567-89ab-cdef-0123-456789abcdef",
                    name   : "Peter Parker",
                    email  : "peter.parker@email.com",
                    type   : "OPERATOR",
                    enabled: true
                ]
            ]
        ])
    }
}