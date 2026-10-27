package contracts.user

import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.MediaType

Contract.make {
    request {
        method GET()
        headers {
            accept(MediaType.APPLICATION_JSON_VALUE)
        }
        url("/api/v1/users/00000111-2200-0033-2200-444000000022")
    }
    response {
        status NOT_FOUND()
        headers {
            contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
        }
        body([
            type    : "/errors/not-found",
            title   : "Not Found",
            status  : 404,
            detail  : "User not found with ID: 00000111-2200-0033-2200-444000000022",
            instance: fromRequest().path()
        ])
    }
}