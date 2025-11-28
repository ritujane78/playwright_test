Feature: Product Catalog

  As a customer,
  I want to easily search, filter and sort products in the catalog
  So I can find what I need quickly.

  Sally is an online shopper.

  Rule: Customers should be able to search for products by name

    Example: The one where Sally searches for an Adjustable Wrench
      Given Sally is on the home page
      When she searches for "Adjustable Wrench"
      Then the "Adjustable Wrench" product should be displayed

    Example: The one where Sally searches for a general term
      Given Sally is on the home page
      When she searches for "saw"
      Then the following products should be displayed:
        | Product | Price |
        | Wood Saw      | $12.18 |
        | Circular Saw  | $80.19   |

    Example: The one where Sally only wants to see Power Drills
      Given Sally is on the home page
      When she searches for "drill"
      And she filters by "Power Tools"
      Then the following products should be displayed:
        | Product  | Price  |
        | Cordless Drill 24V | $66.54 |
        | Cordless Drill 12V | $46.50 |

  Rule: Customers should be able to sort products by various criteria
    Scenario Outline: Sally sorts by different criteria
      Given Sally is on the home page
      When she sorts by "<Sort>"
      Then the first product displayed should be "<First Product>"
      Examples:
        | Sort               | First Product       |
        | Name (A - Z)       | Adjustable Wrench   |
        | Name (Z - A)       | Wood Saw            |
        | Price (High - Low) | Drawer Tool Cabinet |
        | Price (Low - High) | Washers             |
