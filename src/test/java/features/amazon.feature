Feature: Amazon practice
  Scenario: Login
    * Navigate to https://www.amazon.com/
    * Go to login section
    * Submit the following info:
      | Email    | sample@gmail.com |
      | Password | ****             |
    * Wait 3 seconds

  Scenario: Save link info on selected section
    * Navigate to https://www.amazon.com/
    * Click on drop down menu button and expend the targeted section
    * Create a text file and save link info on the section named Electronics from the drop down menu on the landing page
    * Create a text file and save link info on the section named Computers from the drop down menu on the landing page
    * Create a text file and save link info on the section named Smart Home from the drop down menu on the landing page
    * Create a text file and save link info on the section named Arts & Crafts from the drop down menu on the landing page
    * Wait 3 seconds