# Nutritional-assistant
Simple android application for nutrition control and weight goal achievement with adaptive meal plan generation created in _Java_ with _Android SDK_.

Biggest asset of this application in comparison to similar nutrition control/manager apps on market is that users don't have to strictly obey given meal plan. Instead, they can deviate from it if needed and still be able to meet their nutrition limits and goals by using application's adaptive meal plan re-generation.

## Quick overview
Application helps it's users to get dreamed weight in relatively painless, straightforward and healthy way. User provides 6 parameters (sex, age, weight, height, lifestyle, weight goal), from which are daily nutrition intake limits calculated to meet chosen weight goal. Afterwards, user can generate daily meal plan that many times as he needs, to be finally happy with the one generated (beware of API request limit (tu bude anchor) in this version of app). Generated meal plan satisfies nutrition limits as much as possible. Application also supports adding foods from provided databases manually. Adding food manually breaks generated meal plan, because it changes assumed nutrition intake. User can deal with such sitations by using application's adaptive re-generation of meal plan, so it's now possible for user to add his own foods to meals and be sure that in the end, his nutrition limits will be satisfied.

## User manual

### Main activity
On main screen you see overview of your daily food intake an generated meal plan. You got four main options from this activity:

1. Add food (floating button)
2. Reset your daily income
3. Settings
4. (Re-)Generate meal plan

### Screenshots
TODO Main activity screenshots

#### Add food
When you click food adding button, application gets you to an activity, where you have search bar.
You can start typing and search bar will provide you with suggestions to what are you writing.
After you choose what you want to eat or have eaten, dialog window will pop up on you with number picker to choose grams of your food
and you can add this food to your daily income.

#### Reset your daily income
From main screen you got option (in action bar) to reset your daily food income. Simply press the button and you are back to 0s.

### Settings
Clicking Settings action button presents you with 4 options:

1. Nutrition settings
2. Database (Work in progress)
3. Intake overview (TODO)
4. User parameters

#### Nutrition settings
In nutrition settings you can set your daily nutrition limits. You can choose from manual or automatic setting.
From manual you can set limits manually, but most of time, automatic is sufficent. If you choose automatic, then you are asked about your current lifestyle
(sedentary, mild activity, medium activity, high activity) and what is your fitness goal (gain, lose, maintain). After entering these, app calculates you your daily limit calories.
Prerequisite to this automatic calculation is entering your parameters in User parameters acitivty. If you haven't entered them, app won't let you do automatic calculation
and will ask you to enter them.

#### Database (Work in progress)
You can filter foods by calories, fats, carbohydrates and proteins. From given filter variables, database presents you with food sufficing these constrains.

#### Intake overview (TODO)
Here you will be able to see your intake graphs in long term and check on how you keep to your diet.
Again, there will be more categories (calories, fats, carbohydrates, proteins).

#### User parameters
To app function correctly when using automatic nutrition settings, you are required to enter your physical parameters, here.

### Minimum API Version
* 23 (Android 6.0 Marshmallow)

## TODO
* Implement Database
* Implement Intake overview
* Use [View Binding](https://developer.android.com/topic/libraries/view-binding) :heavy_check_mark:
* Make activity hierarchy in AndroidManifest.xml to be able to use "back" action button
* In food adding dialog popup change number picker to number entering (number picker was used just for personal learning purposes)
* Use external solution with API for foods (right now I'm using just local database with cca. 1K of entries)

## Tested on devices
* Nexus 5X (API 29, x86 emulator)
* Huawei P8 Lite (Android 6.0, API 23, physical device)

## Database
Powered by [Nutritionix](https://www.nutritionix.com/business/api) & [Spoonacular](https://spoonacular.com/food-api) API
