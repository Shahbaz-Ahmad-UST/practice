package com.ust.tripStack.tests;

import com.ust.tripStack.api.client.*;

import com.ust.tripStack.report.ExtentTestListener;
import com.ust.tripStack.support.TestEnvironment;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("tripStack Journeys")
@Feature("Full Booking Journey")
@Owner("Shahbaz Ahmad")
@ExtendWith(ExtentTestListener.class)
public class BookingE2E {
    AuthClient auth = new AuthClient();
    HomeClient home = new HomeClient();
    SeatClient seat = new SeatClient();
    PaymentClient payment = new PaymentClient();
    TicketClient ticketClient = new TicketClient();




    @Test
    @DisplayName("login start with credentials")
    void testPractice()
    {
        Response res =  auth.login(
        TestEnvironment.required("CUSTOMER_MAIL"),TestEnvironment.required("CUSTOMER_PASSWORD"));


        String token = res.jsonPath().getString("token");

        home.reset(token);

        var resme= auth.authMe(token)
                .then().statusCode(200);
//        System.out.println(resme.extract().jsonPath().prettyPrint());

        var flight = home.searchFlight(token,"BLR","CCU").
                then().extract().response();
        System.out.println(flight.jsonPath().prettyPrint());


        var seatRes = seat.getFlight(token,"FL-BLRCCU-51");

//        System.out.println(seatRes.jsonPath().prettyPrint());
        var seatBooking = seat.booking(token);

        System.out.println(seatBooking.jsonPath().prettyPrint());

        String bookingId =  seatBooking.jsonPath().getString("id");

        var paymentResponse =payment.payment(token,bookingId);


        var conRes = payment.confirm(token,bookingId);

        var get_booking = ticketClient.getBooking(token);
        String pnr = get_booking.jsonPath().getString("pnr");
        System.out.println(get_booking.jsonPath().prettyPrint());

        var getpnr =  ticketClient.getPNR(token,pnr);

        home.reset(token);


    }
}
