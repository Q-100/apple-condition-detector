<div align="center">

![image](https://user-images.githubusercontent.com/68915940/143439231-ed220f69-6d8e-4d84-b049-51e4cb47b11d.png)
<h1><span style="color:indianred">Apple condition detector</span></h1>

<p align="center">
Use yolov5 to detect apple conditions and provide apple rating.

</p>

[![MIT License](https://flat.badgen.net/github/license/tw-in-js/twind)](https://github.com/tw-in-js/twind/blob/main/LICENSE)
[![Latest Release](https://flat.badgen.net/npm/v/twind?icon=npm&label&cache=10800&color=blue)](https://www.npmjs.com/package/twind)
[![Bundle Size](https://flat.badgen.net/bundlephobia/minzip/twind?icon=packagephobia&label&color=blue&cache=10800)](https://bundlephobia.com/result?p=twind 'gzip bundle size (including dependencies)')
[![Package Size](https://flat.badgen.net/badgesize/brotli/https://cdn.jsdelivr.net/npm/twind/twind.min.js?icon=jsdelivr&label&color=blue&cache=10800)](https://unpkg.com/twind/twind.js 'brotli package size (without dependencies)')
[![Documentation](https://flat.badgen.net/badge/icon/Documentation?icon=awesome&label)](https://twind.dev)
[![Github](https://flat.badgen.net/badge/icon/tw-in-js%2Ftwind?icon=github&label)](https://github.com/tw-in-js/twind)
[![Discord](https://flat.badgen.net/badge/icon/discord?icon=discord&label)](https://discord.com/invite/2aP5NkszvD)
[![CI](https://github.com/tw-in-js/twind/workflows/CI/badge.svg)](https://github.com/tw-in-js/twind/actions?query=workflow%3Aci)
[![Coverage Status](https://flat.badgen.net/coveralls/c/github/tw-in-js/twind/main?icon=codecov&label&cache=10800)](https://coveralls.io/github/tw-in-js/twind?branch=main)



The Apple condition detector detects the pests, cuts, bruises, and colors of apples and checks their grades in real time. You can use it if you want to know the grade of an apple or what pests you harvested are.
</div>
<div align="center">
<img src="https://user-images.githubusercontent.com/68915940/143449367-a05e0642-139b-4463-8008-a0bb3b81980a.png"  width="200" height="400"/>
</div>





## [Development Environment]()
- Google colaboratory, local(GPU : 2070super 8GB, RAM : 16GB, CPU : 3700x)
- Yolov5
- Tensorflow 1.15.2v
- pytorch 1.10.0+cu111
- Android Studio @3.5.3
- ---
## [Features]()

**⚡️ Real-time Detection**

Just take a video of an apple and it shows the conditions of the apple in real time.


**🐜 Small object detection**

During the image preprocessing step, even small objects can be detected by increasing the size of the image to 640

![image](https://user-images.githubusercontent.com/68915940/143448929-c3171db6-5a8a-41ba-a60b-5edb8e53061f.png)

**🍎 Detecting various types of apple pests**

It can detect five pests that occur frequently in Fuji(부사사과).

**🌈 Can Differentiate the colors of apples.**

The apple condition detector can detect the color of the apple by dividing it into 3 colors.

**‍🩹Detects many factors that affect apples**

Various types of cuts and bruises that affect apple grade can be detected.


----
## 📀 Apple Dataset

Data and number of annotations used in the apple condition detector :<br>



![image](https://user-images.githubusercontent.com/78460820/142620555-fe57cbc5-a21f-4496-bc2c-5ab80d9bbf04.png)

1. Image : 1533(After Data Augmentation)
2. Class : 11
3. ImageSize : 640
4. Annotations : 7188

Images of apples were crawled by Google and Coupang, and pests used images provided by 🇰🇷[NCPMS](https://ncpms.rda.go.kr/npms/ImageSearchDtlR2.np?kncrCode=FT010601&kncrNm=%EC%82%AC%EA%B3%BC&upperNm=%EA%B3%BC%EC%88%98&flagCode=S&queryFlag=A&nextAction=%2Fnpms%2FImageSearchDtlR2.np).

### Download dataset
You can download apple_grade dataset here: https://app.roboflow.com/ds/GN9aUMJdR1?key=gcxVDmN2be

If you want to use the dataset from colab,

```python
curl -L "https://app.roboflow.com/ds/5FcXgt0BjG?key=ElfkiFKHaY" > roboflow.zip; unzip roboflow.zip; rm roboflow.zip
```
-------
## 📀Apple Class example
1. apple : It is a class to check if it is an apple(Using all kinds of apples)

![image](https://user-images.githubusercontent.com/68915940/143457063-853401a1-59e0-4479-8277-784838a2a5f1.png)
2. apple_A : An apple with superior color(Using only Fuji apples)

![image](https://user-images.githubusercontent.com/68915940/143457080-68842204-2bc1-4c3b-85ce-0f401523936c.png)
3. apple_B : An apple with medium color(Using only Fuji apples)

![image](https://user-images.githubusercontent.com/68915940/143457100-28bc1cf5-4a38-4dfe-91c7-ca8f3b580809.png)
4. apple_C : An apple with bed color(Using only Fuji apples)

![image](https://user-images.githubusercontent.com/68915940/143457118-f5259696-e1fb-4a7b-b9ff-018325113a92.png)
5. dmg_s : Apple with small cuts or weak bruises that can be sold as a prize(Using all kinds of apples)

![image](https://user-images.githubusercontent.com/68915940/143457137-12c01f15-0d7b-46db-8e51-c7ae73135e3d.png)
6. dms_l : Apple with strong cuts or strong bruises that cannot be sold as a prize(Using all kinds of apples)

![image](https://user-images.githubusercontent.com/68915940/143457157-c78e1174-9233-4d4c-96c3-5db7bb6fae4b.png)
7. Scab

![image](https://user-images.githubusercontent.com/68915940/143457168-277bdca6-5b15-4fd4-89e4-e88ea67105e3.png)
8. Anthracnose

![image](https://user-images.githubusercontent.com/68915940/143457194-c89d7ba9-07af-4c1e-a3ba-0ce8ac0113c2.png)
9. Sooty blotch

![image](https://user-images.githubusercontent.com/68915940/143457176-aeca2a35-2bed-4679-a6d4-bc4222b9dc20.png)
10. Fly speck

![image](https://user-images.githubusercontent.com/68915940/143457219-a4aef23d-78f2-46e8-9b54-20b05ebf4951.png)
11. White rot

![image](https://user-images.githubusercontent.com/68915940/143457209-e4d7dd97-5923-4ee6-880b-1b711baa6802.png)



Alternatively try the 🚀 [live and interactive demo](https://esm.codes/#aW1wb3J0IHsgdHcgfSBmcm9tICdodHRwczovL2Nkbi5za3lwYWNrLmRldi90d2luZCcKCmRvY3VtZW50LmJvZHkuaW5uZXJIVE1MID0gYAogIDxtYWluIGNsYXNzPSIke3R3YGgtc2NyZWVuIGJnLXB1cnBsZS00MDAgZmxleCBpdGVtcy1jZW50ZXIganVzdGlmeS1jZW50ZXJgfSI+CiAgICA8aDEgY2xhc3M9IiR7dHdgZm9udC1ib2xkIHRleHQoY2VudGVyIDV4bCB3aGl0ZSBzbTpncmF5LTgwMCBtZDpwaW5rLTcwMClgfSI+VGhpcyBpcyBUd2luZCE8L2gxPgogIDwvbWFpbj4KYA==) and take a look at the [installation guide](https://twind.dev/handbook/getting-started).

Twind is also available as an [NPM package](https://www.npmjs.com/package/twind):

```
npm i twind
```

For seamless integration with existing Tailwind HTML you can use [twind/shim](https://twind.dev/handbook/the-shim.html):

```html
<script type="module" src="https://cdn.skypack.dev/twind/shim"></script>

<main class="h-screen bg-purple-400 flex items-center justify-center">
  <h1 class="font-bold text(center 5xl white sm:gray-800 md:pink-700)">This is Twind!</h1>
</main>
```

Try `twind/shim` in the 🚀 [live and interactive shim demo](https://esm.codes/#aW1wb3J0ICdodHRwczovL2Nkbi5za3lwYWNrLmRldi90d2luZC9zaGltJwoKZG9jdW1lbnQuYm9keS5pbm5lckhUTUwgPSBgCiAgPG1haW4gY2xhc3M9Imgtc2NyZWVuIGJnLXB1cnBsZS00MDAgZmxleCBpdGVtcy1jZW50ZXIganVzdGlmeS1jZW50ZXIiPgogICAgPGgxIGNsYXNzPSJmb250LWJvbGQgdGV4dChjZW50ZXIgNXhsIHdoaXRlIHNtOmdyYXktODAwIG1kOnBpbmstNzAwKSI+CiAgICAgIFRoaXMgaXMgVHdpbmQhCiAgICA8L2gxPgogIDwvbWFpbj4KYA==)

This is just the beginning of all the awesome things you can do with Twind. [Check out the handbook](https://twind.dev/handbook/introduction.html) to learn more.

## 💡 Inspiration

It would be untrue to suggest that the design here is totally original. Other than the founders' initial attempts at implementing such a module ([oceanwind](https://github.com/lukejacksonn/oceanwind) and [beamwind](https://github.com/kenoxa/beamwind)) we are truly standing on the shoulders of giants.

- [Tailwind](https://tailwindcss.com/): created a wonderfully thought out API on which the compiler's grammar was defined.
- [styled-components](https://styled-components.com/): implemented and popularized the advantages of doing CSS-in-JS.
- [htm](https://github.com/developit/htm): a JSX compiler that proved there is merit in doing runtime compilation of DSLs like JSX.
- [goober](https://github.com/cristianbote/goober): an impossibly small yet efficient CSS-in-JS implementation that defines critical module features.
- [otion](https://github.com/kripod/otion): the first CSS-in-JS solution specifically oriented around handling CSS in an atomic fashion.
- [clsx](https://github.com/lukeed/clsx): a tiny utility for constructing class name strings conditionally.
- [style-vendorizer](https://github.com/kripod/style-vendorizer): essential CSS prefixing helpers in less than 1KB of JavaScript.
- [CSSType](https://github.com/frenic/csstype): providing autocompletion and type checking for CSS properties and values.

## 🙏🏾 Sponsors

Support us with a monthly donation and help us continue our activities.

[[GitHub Sponsor](https://github.com/sponsors/tw-in-js) | [Open Collective](https://opencollective.com/twind)]

<a href="https://github.com/jordwalke" target="_blank"><img style="border-radius: 50%!important" src="https://avatars.githubusercontent.com/u/977348?v=4" width="64" height="64" alt="@jordwalke"></a>
<a href="https://github.com/tylerforesthauser" target="_blank"><img style="border-radius: 50%!important" src="https://avatars.githubusercontent.com/u/1226786?v=4" width="64" height="64" alt="@tylerforesthauser"></a>
<a href="https://github.com/holic" target="_blank"><img style="border-radius: 50%!important" src="https://avatars.githubusercontent.com/u/508855?v=4" width="64" height="64" alt="@holic"></a>
<a href="https://github.com/Andrewnt219" target="_blank"><img style="border-radius: 50%!important" src="https://avatars.githubusercontent.com/u/52666982?v=4" width="64" height="64" alt="@Andrewnt219"></a>
<a href="https://opencollective.com/twind/backer/0/website" target="_blank"><img src="https://opencollective.com/twind/backer/0/avatar.svg"></a>
<a href="https://opencollective.com/twind/backer/1/website" target="_blank"><img src="https://opencollective.com/twind/backer/1/avatar.svg"></a>
<a href="https://opencollective.com/twind/backer/2/website" target="_blank"><img src="https://opencollective.com/twind/backer/2/avatar.svg"></a>

<a href="https://opencollective.com/twind/sponsor/0/website" target="_blank"><img src="https://opencollective.com/twind/sponsor/0/avatar.svg"></a>
<a href="https://opencollective.com/twind/sponsor/1/website" target="_blank"><img src="https://opencollective.com/twind/sponsor/1/avatar.svg"></a>

## 🤝 Contributing

We are excited that you are interested in contributing to this project! We've put together a whole [contribution guide](https://github.com/tw-in-js/twind/blob/main/CONTRIBUTING.md) to get you started.

## ⚖️ License

The [MIT license](https://github.com/tw-in-js/twind/blob/main/LICENSE) governs your use of Twind.