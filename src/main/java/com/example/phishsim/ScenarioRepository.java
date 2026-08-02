package com.example.phishsim;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ScenarioRepository {

    private final List<Scenario> scenarios = List.of(
            new Scenario(
                    1,
                    "Fake account password reset",
                    "Account Support",
                    "security-alert@micros0ft-support.invalid",
                    "Unusual sign-in activity detected",
                    """
                    Hi,

                    We detected unusual sign-in activity. Verify within 24 hours:

                    https://micros0ft-support.invalid/verify
                    """.trim(),
                    true,
                    List.of(
                            "Lookalike domain (micros0ft with a zero)",
                            "Urgency and threat of suspension",
                            "Generic greeting",
                            "Non-official domain"
                    ),
                    "Open the official portal from a saved bookmark and review recent sign-ins.",
                    Difficulty.BEGINNER
            ),
            new Scenario(
                    2,
                    "University IT maintenance notice",
                    "Example University IT",
                    "helpdesk@university.example",
                    "Planned maintenance window tonight",
                    """
                    Hello,

                    Authentication maintenance is planned tonight from 11:00 PM to 1:00 AM.
                    Contact the IT Help Desk through the campus portal if issues continue.
                    """.trim(),
                    false,
                    List.of(
                            "No request for sensitive information",
                            "Expected institutional domain and calm tone",
                            "Known support channel"
                    ),
                    "Confirm maintenance through the university portal or status page.",
                    Difficulty.INTERMEDIATE
            ),
            new Scenario(
                    3,
                    "Package delivery scam",
                    "Package Delivery",
                    "alerts@ups-tracking-secure.invalid",
                    "Delivery failed: action required",
                    """
                    We could not complete delivery. Confirm your address and pay the re-delivery fee:

                    http://ups-tracking-secure.invalid/update-address
                    """.trim(),
                    true,
                    List.of(
                            "Unexpected fee",
                            "Unencrypted HTTP link",
                            "Unofficial sender domain",
                            "Pressure to act"
                    ),
                    "Open the carrier's official app and look up the tracking number there.",
                    Difficulty.BEGINNER
            ),
            new Scenario(
                    4,
                    "Informational bank alert",
                    "Example Bank Alerts",
                    "alerts@bank.example",
                    "New device sign-in detected",
                    """
                    We noticed a sign-in from a new device. If this was you, no action is needed.
                    Otherwise, open your banking app. We never ask for your password by email.
                    """.trim(),
                    false,
                    List.of(
                            "Does not ask for credentials",
                            "Directs you to the official app",
                            "Clear safety reminder"
                    ),
                    "Open the bank's official app directly and review account activity.",
                    Difficulty.ADVANCED
            ),
            new Scenario(
                    5,
                    "Payroll direct-deposit change",
                    "Payroll Services",
                    "payroll-update@employee-payments.invalid",
                    "Direct deposit verification required",
                    """
                    Your next paycheck is on hold because your banking details could not be verified.
                    Confirm your routing number and account information before 3:00 PM:

                    https://employee-payments.invalid/direct-deposit
                    """.trim(),
                    true,
                    List.of(
                            "Requests sensitive banking information",
                            "Threatens to delay a paycheck",
                            "Uses an unfamiliar external domain",
                            "Imposes an unusually short deadline"
                    ),
                    "Contact payroll using the number in the employee directory, not the message.",
                    Difficulty.INTERMEDIATE
            ),
            new Scenario(
                    6,
                    "Routine benefits enrollment reminder",
                    "Example Company People Team",
                    "people-team@company.example",
                    "Open enrollment closes Friday",
                    """
                    Open enrollment closes Friday at 5:00 PM. Review your current elections in the
                    employee portal. If you need help, attend Thursday's benefits Q&A or contact the
                    People Team through the company directory.
                    """.trim(),
                    false,
                    List.of(
                            "Uses the expected company domain",
                            "Directs employees to the known internal portal",
                            "Provides established support options",
                            "Does not request information by email"
                    ),
                    "Navigate to the employee portal from the company intranet to review benefits.",
                    Difficulty.BEGINNER
            ),
            new Scenario(
                    7,
                    "Unexpected shared document",
                    "Project Collaboration",
                    "sharing@cloud-docs-notice.invalid",
                    "A confidential document was shared with you",
                    """
                    A confidential document has been shared with your account.

                    Sign in to view it before access expires:
                    https://cloud-docs-notice.invalid/shared/quarterly-review
                    """.trim(),
                    true,
                    List.of(
                            "No sender or project context",
                            "Uses a generic file-sharing domain",
                            "Creates urgency with expiring access",
                            "Requests a sign-in after an unexpected share"
                    ),
                    "Open the organization's approved document service directly and check shared files.",
                    Difficulty.INTERMEDIATE
            ),
            new Scenario(
                    8,
                    "Security team MFA notice",
                    "Example Company Security",
                    "security@company.example",
                    "Multiple sign-in prompts reported",
                    """
                    We are investigating reports of unexpected sign-in approval prompts.
                    Do not approve a prompt you did not initiate. Report unexpected prompts with the
                    Security button in the company portal. No reply to this message is required.
                    """.trim(),
                    false,
                    List.of(
                            "Provides preventive guidance without requesting approval",
                            "Uses the expected company domain",
                            "Directs reports through a known internal channel",
                            "Does not include a sign-in link"
                    ),
                    "Reject unexpected prompts and report them through the official security channel.",
                    Difficulty.INTERMEDIATE
            ),
            new Scenario(
                    9,
                    "Executive gift-card request",
                    "Jordan Lee, CEO",
                    "jordan.lee.ceo@executive-mail.invalid",
                    "Need a quick favor before my meeting",
                    """
                    I'm heading into a meeting and need you to purchase six gift cards for a client.
                    Send the card numbers and PINs by replying to this email. Please keep this between us.

                    I need them within the hour.

                    —Jordan
                    https://executive-mail.invalid/request
                    """.trim(),
                    true,
                    List.of(
                            "Unusual financial request from an executive",
                            "Requests gift-card numbers and PINs",
                            "Demands secrecy and urgency",
                            "Sender address does not use the company domain"
                    ),
                    "Verify the request with the executive through a known phone number or assistant.",
                    Difficulty.ADVANCED
            ),
            new Scenario(
                    10,
                    "Completed expense report receipt",
                    "Example Company Expenses",
                    "expenses@company.example",
                    "Expense report ER-1042 approved",
                    """
                    Expense report ER-1042 has been approved. The reimbursement will appear in your
                    next scheduled payment. You can view the report from the Expenses section of the
                    employee portal. No action is required.
                    """.trim(),
                    false,
                    List.of(
                            "References a specific report number",
                            "Does not request payment or credentials",
                            "Directs the user to the known employee portal",
                            "Uses the expected company domain"
                    ),
                    "If the report is unfamiliar, verify it from the employee portal without using email links.",
                    Difficulty.ADVANCED
            ),
            new Scenario(
                    11,
                    "Fake calendar cancellation",
                    "Meeting Notifications",
                    "calendar-update@meeting-tools.invalid",
                    "Interview canceled — review new time",
                    """
                    Your interview has been canceled. The organizer proposed a new confidential time.
                    Sign in immediately to keep your interview slot:

                    https://meeting-tools.invalid/reschedule
                    """.trim(),
                    true,
                    List.of(
                            "Unexpected high-stakes schedule change",
                            "Generic organizer identity",
                            "Pressures the recipient to sign in immediately",
                            "Uses an unfamiliar scheduling domain"
                    ),
                    "Check the original calendar event or contact the recruiter using prior correspondence.",
                    Difficulty.ADVANCED
            ),
            new Scenario(
                    12,
                    "Vendor maintenance confirmation",
                    "Example Cloud Status",
                    "status@service.example",
                    "Maintenance completed successfully",
                    """
                    Scheduled maintenance completed at 02:15 UTC. All systems are operating normally.
                    No customer action is required. Current service status remains available from the
                    status page bookmarked in your administrator console.
                    """.trim(),
                    false,
                    List.of(
                            "Informational message with no requested action",
                            "Provides a specific completion time",
                            "Uses the expected service domain",
                            "References a known status page rather than embedding a link"
                    ),
                    "Confirm service status from the administrator console if operational impact is suspected.",
                    Difficulty.BEGINNER
            )
    );

    public List<Scenario> findAll() {
        return scenarios;
    }

    public Optional<Scenario> findById(int id) {
        return scenarios.stream().filter(scenario -> scenario.id() == id).findFirst();
    }
}
