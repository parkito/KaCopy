<h1 align="center">
  <br>
   <img src="https://s8.hostingkartinok.com/uploads/images/2018/03/68b491a97174353014a1f92aaecba400.png" alt="Logo ArrayMixer" title="KaCopy logo" />
  <br>
</h1>
<p align="center">  
<a href="https://www.codacy.com/app/josetelesmaciel/array-mixer?utm_source=github.com&utm_medium=referral&utm_content=teles/array-mixer&utm_campaign=badger"><img src="https://api.codacy.com/project/badge/Grade/2cbd62dd3c284ce79f6e2c35817bec12"></a>
<a href="https://www.codacy.com/app/josetelesmaciel/array-mixer?utm_source=github.com&utm_medium=referral&utm_content=teles/array-mixer&utm_campaign=Badge_Coverage"><img src="https://api.codacy.com/project/badge/Coverage/8a941e0f57c047c8a481f4854666b42d"></a>
<a href="https://travis-ci.org/teles/array-mixer"><img src="https://travis-ci.org/teles/array-mixer.svg?branch=master"></a>
<a href="https://gitter.im/array-mixer/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge"><img src="https://badges.gitter.im/array-mixer/Lobby.svg"></a>
 <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/license-MIT-blue.svg"></a>
</p>

<p align="center">
  <strong>KaCopy</strong> is the library for deep copying java objects.
</p>

## Table of contents

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3ae97363f92749bdb49886be719c5364)](https://app.codacy.com/app/parkito/KaCopy?utm_source=github.com&utm_medium=referral&utm_content=parkito/KaCopy&utm_campaign=badger)


  * [Common usage](#common-usage)
  * [Installation](#installation)
     * [Gradle](#gradle)
     * [Maven](#maven)
  * [Api](#api)
     * [OOP](#aliases)
     * [Utils](#sequence)
  * [Versions](#versions)
     * [Version table](#aliases)
  * [Examples](#examples)
     * [Example 1) For every 7 photos display an ad:](#example-1-for-every-7-photos-display-an-ad)
     * [More examples](#more-examples)
  * [Contributing](#contributing)
    * [Contributing](#contributing)
  * [License](#license)

## Common usage

Let's think we have two arrays:  **photos** and **ads**.

```javascript
photos.length === 12; // true
ads.length === 6; // true
```

Use `ArrayMixer` to create a new array containing **2 photos** followed by **1 ad** until the end of both arrays.


## Installation

### Maven

```bash
npm install array-mixer --save
```

### Gradle

```bash
npm install array-mixer --save
```

## API

### OOP

### Utils


## Versions

## Examples

`ArrayMixer` can be used combining different arrays, aliases and sequences.
The following examples shows some ways to use it.

### Example 1) For every 7 photos display an ad:

```javascript
ArrayMixer({F: Photos, A: Ads}, ["7P", "1A"]);
```
**or** (as number 1 on sequence can be ommited):

```javascript
ArrayMixer({F: Photos, A: Ads}, ["7P", "A"]);
```
> **Disclaimer**: All arrays mentioned in this section must exist for the examples to work.

### More examples

For more example please check the [specification file](src/spec.js).

## Contributing

You may contribute in several ways like creating new features, fixing bugs, improving documentation and examples
or translating any document here to your language. [Find more information in CONTRIBUTING.md](CONTRIBUTING.md).

## License

[MIT](https://github.com/parkito/KaCopy/blob/master/LICENSE)
