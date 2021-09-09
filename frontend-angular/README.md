# MBTI-ASSESSMENT WEB CLIENT WITH ANGULAR

This is the UI part of the Lunatech MBTI Assessment Project.

It uses Angular Plateform with MDB

### Angular 12 & Bootstrap 5 & Material Design 2.0 UI KIT

These are the main installed libraries for beguinning :

- **[Angular 12](https://angular.io/)**
- **[Bootstrap 5 & Material Design 2.0 UI KIT](https://mdbootstrap.com/docs/b5/angular#demo)**

## Project setup

### Font

We use **Fira-sans** font. To install from **npm**, just hint :

```sh
$ npm install @fontsource/fira-sans
```

After that, import it in our global (the entry style sheet of the app) **`src/style.scss`** :

```scss
@import "~@fontsource/fira-sans"; // Defaults to weight 400.
```

### Environemnt Variables

- We want to pass **environment variables** at building time (either prodction or development building time) in an Angular application. We do so by using **`.env`** files, we can create as many **`.env`** as we need.
- It allows to deploy the same application but with a different configuration file.
- We setup **dotenv** to Access Environment Variables in Angular.
  > _Dotenv is a zero-dependency module that loads environment variables from a .env file into process.env._
- It will take variables defined in a .env file, and injects them into Node’s process.env object. We use then the injected values from Node to create dynamically the **`environment.ts`**.

#### Process steps

1. Install **yargs** and **dotenv**.

```sh
npm install ts-node yargs dotenv colors
```

2. Create a **`.env`** file at the root of the project folder and populate it with the variables.
3. Write a script (**`setenv.ts`** that we put in the root folder of the Angular project) which creates the required environment file (**`environment.ts`** for development and **`environment.prod.ts`** for production) and populates it with the variables from the .env file (available in **`process.env`**).
4. Run the script before running **`ng serve`** or **`ng build`**. (Teawk a little bit the **`package.json`** for that purpose as following).

```json
{
   ...
   "scripts": {
      "ng": "ng",
      "config": "ts-node setenv.ts",
      "start": "npm run config -- --environment=dev && ng serve",
      "build": "npm run config -- --environment=prod && ng build --configuration production",
      ...
   },
   ...
}
```

> [Read more](https://ferie.medium.com/how-to-pass-environment-variables-at-building-time-in-an-angular-application-using-env-files-4ae1a80383c) and [this article](https://javascript.plainenglish.io/setup-dotenv-to-access-environment-variables-in-angular-9-f06c6ffb86c0).

Then use **`npm start or npm run-script build`**.

### NgRX for State Management

Install these libraries for the https://ngrx.io/ :

```sh
$ ng add @ngrx/store@latest
$ ng add @ngrx/store-devtools@latest
$ ng add @ngrx/effects@latest
$ ng add @ngrx/entity@latest
$ ng add @ngrx/schematics@latest
$ ng add @ngrx/router-store@latest
```

Some snippets from the src/app folder :

- Generate the initial state management files within a store folder and register it within the app.module.ts.

```
$ ng g store State --root --state-path=shared/store --module=app.module.ts --state-interface=AppState
```

- Generate a `Auth` feature set within the `features/auth` folder and register it with the `auth.module.ts` file in the same `features/auth` folder.

```
 $ ng g feature features/auth/Auth --reducers ../../shared/store/index.ts --module features/auth/auth.module.ts --api true --skip-tests true
```

### Angular Auth0 JWT

Install

```
$ npm i @auth0/angular-jwt
```

Features :

- JwtHelper will take care of helping you decode the token and check its expiration date.
- Decode a JWT from the Angular app
- Check the expiration date of the JWT
- Automatically send the JWT in every request made to the server
- Manage the user's authentication state with authManager

### Ngx Spinner

Install

```
$ ng add ngx-spinner
```

Features :

- **Angular 12** Support
- Multiple Spinners
- Configurable option through service
- Fullscreen Mode(Enable/Disable)
- `show()/hide()` methods return promise
- Smooth animation while `hide/show` the spinner
- Option to disable fade animation
- `Show/Hide` spinner from template using @Input() variable

#### Ngx Notification Message

Install

```
$ npm install ngx-notification-msg
```

Angular Library to display notification message.
