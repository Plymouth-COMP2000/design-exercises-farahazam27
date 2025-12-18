Restaurant Management Application

## Project Overview
This is a mobile application developed as part of the COMP2000 Software Engineering 2 module coursework. The application provides a comprehensive restaurant management system with separate interfaces for Staff and Guest users.

## Module Information
- **Module Code:** MAL2017 (COMP2000)
- **Module Title:** Software Engineering 2
- **Module Leader:** Dr Ang Jin Sheng
- **Assessment Type:** 100% Coursework
  - Assessment 1 (30%): Design Exercises
  - Assessment 2 (70%): Implementation

## Application Description
The Restaurant Management Application enables efficient management of restaurant operations through two distinct user roles:

### Staff Side Features
- Add, edit, and delete menu items (with name, price, and image)
- View and manage customer reservations
- Cancel reservations when necessary
- Receive notifications for new and modified reservations

### Guest Side Features
- Browse restaurant menu with prices and images
- Make table reservations for specific dates and times
- Edit or cancel existing reservations
- Receive notifications when reservation status changes

## Technical Implementation

### Technologies Used
- **Language:** Java
- **IDE:** Android Studio
- **Database:** 
  - SQLite (local storage for menu and reservations)
  - RESTful API integration for user authentication and centralized data
- **Version Control:** Git & GitHub

### Key Technical Features
- Role-based access control (Staff/Guest authentication)
- Multi-threaded API connections (worker threads for non-blocking UI)
- Local database management with SQLite
- Push notifications with user-customizable preferences
- Responsive UI supporting multiple screen sizes and orientations
- Implementation of SOLID principles and design patterns

## Design Principles Applied
- Human-Computer Interaction (HCI) principles
- Standard design patterns
- SOLID programming principles
- Usability-centered design approach

## Development Process
This project follows an iterative design and development process:

1. **User Analysis & Requirements Gathering**
2. **Low-Fidelity Prototyping** (paper sketches/wireframes)
3. **Formative Usability Evaluation** (with representative users)
4. **UI Refinement** based on feedback
5. **High-Fidelity Implementation** in Android Studio
6. **Full Application Development**
7. **Summative Usability Testing**

## API Integration
The application connects to a RESTful API for secure user authentication and data management. API documentation is available through the module resources.

- API Documentation: http://10.240.72.69/comp2000/coursework/docs#/

## Setup Instructions
1. Clone this repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Configure API endpoint settings (if necessary)
5. Run the application on an emulator or physical device

## Testing
- Usability testing conducted with representative users
- Functionality testing across typical and edge-case scenarios
- Responsive design testing on multiple device sizes

## Use of Generative AI
This project was developed with **Assisted Work** level AI usage, as permitted by the module guidelines. AI tools were used for:
- Idea Generation and Problem Exploration (A1)
- Planning & Structuring Projects (A2)
- Research Assistance (A4)
- Language Refinement (A5)
- Technical Guidance & Debugging Support (A8)

All AI usage has been declared in accordance with university policy and included in the submitted documentation.

## Submission Components
1. **Design Report** - PDF document detailing design process and decisions
2. **Implementation Report** - PDF document covering technical implementation
3. **Video Presentations** - Demonstration videos showcasing design and functionality
4. **Source Code** - Complete Android Studio project (this repository)

## Assessment Deadlines
- Design Report: 5 December 2025 (15:00)
- Implementation Report: 19 December 2025 (15:00)

## Academic Integrity
This project adheres to the University of Plymouth's academic integrity policies. All external code sources and AI assistance have been properly documented and referenced.

## Author
[NUR FARAH AQILAH BINTI NORAZAM]
[BSCS2509261]
=

## License
This project is submitted as academic coursework for MAL2017 Software Engineering 2.

---

**Note:** This repository is maintained as part of the assessment requirements and demonstrates incremental development through version control commits from initial design to final implementation.
