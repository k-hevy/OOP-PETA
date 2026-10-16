
package presentationlayer;

import datalayer.Repository;
import entitylayer.SupportAgent;
import entitylayer.Ticket;
import entitylayer.Comment;
import java.util.ArrayList;
import java.util.Scanner;

public class SupportAgentMenu extends BaseMenu {

    private SupportAgent supportAgent;

    public SupportAgentMenu(SupportAgent supportAgent, Scanner input, Repository repository) {
        super(supportAgent, input, repository);
        this.supportAgent = supportAgent;
    }

    @Override
    protected void displayMenu() {
        System.out.println("SUPPORT AGENT MENU");
        System.out.println("[1] View Assigned Tickets");
        System.out.println("[2] View Ticket Details");
        System.out.println("[3] Update Ticket Status");
        System.out.println("[4] Change Priority");
        System.out.println("[5] Add Comment");
        System.out.println("[6] View My Resolved Count");
        System.out.println("[0] Logout");
    }

    @Override
    protected void executeChoice(int choice) {
        String ticketId;
        Ticket ticket;

        switch (choice) {

            case 1:
                ArrayList<Ticket> tickets = supportAgent.getAssignedTickets();

                if (tickets.isEmpty()) {
                    System.out.println("\nNo assigned tickets.");
                } else {
                    System.out.println("\n===== ASSIGNED TICKETS =====");

                    for (Ticket t : tickets) {
                        System.out.println("------------------------------");
                        System.out.println("Ticket ID: " + t.getTicketId());
                        System.out.println("Subject: " + t.getSubject());
                        System.out.println("Priority: " + t.getPriority());
                        System.out.println("State: " + t.getState());
                    }

                    System.out.println("------------------------------");
                }
                break;

            case 2:
                System.out.print("Enter Ticket ID: ");
                ticketId = input.nextLine();

                ticket = supportAgent.getTicket(ticketId);

                if (ticket == null) {
                    System.out.println("Ticket not found or not assigned to you.");
                } else {
                    System.out.println("\n===== TICKET DETAILS =====");
                    System.out.println(ticket.getFormattedTicket());
                    System.out.println("Subject        : " + ticket.getSubject());

                    System.out.println("\n===== COMMENTS =====");

                    ArrayList<Comment> comments = ticket.getComments();

                    if (comments.isEmpty()) {
                        System.out.println("No comments yet.");
                    } else {
                        for (Comment comment : comments) {
                            System.out.println(comment.getFormattedComment());
                        }
                    }
                }
                break;

            case 3:
                System.out.print("Enter Ticket ID: ");
                ticketId = input.nextLine();

                ticket = supportAgent.getTicket(ticketId);

                if (ticket == null) {
                    System.out.println("Ticket not found or not assigned to you.");
                    break;
                }

                System.out.println("\nCurrent Status: " + ticket.getState());
                System.out.println("Available Status Changes:");

                if (ticket.getState().equalsIgnoreCase("OPEN")) {
                    System.out.println("[1] IN_PROGRESS");
                } else if (ticket.getState().equalsIgnoreCase("IN_PROGRESS")) {
                    System.out.println("[1] RESOLVED");
                    System.out.println("[2] CANCELLED");
                } else if (ticket.getState().equalsIgnoreCase("RESOLVED")) {
                    System.out.println("[1] IN_PROGRESS");
                    System.out.println("[2] CLOSED");
                } else {
                    System.out.println("No status changes available.");
                    break;
                }

                System.out.print("Choose New Status: ");
                String statusChoice = input.nextLine();

                String newState = "";

                if (statusChoice.equals("1")) {
                    if (ticket.getState().equalsIgnoreCase("OPEN")) {
                        newState = "IN_PROGRESS";
                    } else if (ticket.getState().equalsIgnoreCase("IN_PROGRESS")) {
                        newState = "RESOLVED";
                    } else if (ticket.getState().equalsIgnoreCase("RESOLVED")) {
                        newState = "IN_PROGRESS";
                    }
                } else if (statusChoice.equals("2")) {
                    if (ticket.getState().equalsIgnoreCase("IN_PROGRESS")) {
                        newState = "CANCELLED";
                    } else if (ticket.getState().equalsIgnoreCase("RESOLVED")) {
                        newState = "CLOSED";
                    }
                } //pachange if this is not right, thanks

                if (newState.isEmpty()) {
                    System.out.println("Invalid status choice.");
                    break;
                }

                System.out.print("Enter reason for status change: ");
                String reason = input.nextLine();

                if (reason.trim().isEmpty()) {
                    System.out.println("Reason cannot be empty.");
                } else if (supportAgent.updateTicketState(ticketId, newState, reason)) {
                    System.out.println("Ticket status updated successfully.");
                } else {
                    System.out.println("Unable to update ticket status.");
                }
                break;

            case 4:
                System.out.print("Enter Ticket ID: ");
                ticketId = input.nextLine();

                ticket = supportAgent.getTicket(ticketId);

                if (ticket == null) {
                    System.out.println("Ticket not found or not assigned to you.");
                    break;
                }

                System.out.println("Current Priority: " + ticket.getPriority());
                System.out.println("Available Priorities: URGENT, HIGH, MEDIUM, LOW");
                System.out.print("Enter new priority: ");
                String newPriority = input.nextLine().trim().toUpperCase();

                if (supportAgent.changePriority(ticketId, newPriority)) {
                    System.out.println("Priority changed successfully.");
                } else {
                    System.out.println("Unable to change priority. Check the priority value or current priority.");
                }
                break;

            case 5:
                System.out.print("Enter Ticket ID: ");
                ticketId = input.nextLine();

                ticket = supportAgent.getTicket(ticketId);

                if (ticket == null) {
                    System.out.println("Ticket not found or not assigned to you.");
                    break;
                }

                System.out.print("Enter comment: ");
                String message = input.nextLine();

                if (message.trim().isEmpty()) {
                    System.out.println("Comment cannot be empty.");
                } else if (supportAgent.addComment(ticketId, message)) {
                    System.out.println("Comment added successfully.");
                } else {
                    System.out.println("Unable to add comment.");
                }
                break;

            case 6:
                System.out.println("\n===== RESOLVED TICKETS =====");
                System.out.println("Your resolved ticket count: "
                        + supportAgent.getTicketsResolvedCount());
                break;

            case 0:
                System.out.println("Logging out...");
                //stop();
                break;

            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    @Override
    protected int getMinChoice() {
        return 0;
    }

    @Override
    protected int getMaxChoice() {
        return 6;
    }
}
