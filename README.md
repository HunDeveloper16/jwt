
## ๐ JWT๋ฅผ ์ฐ๋ ์ด์ 
์ฌ์ฉ์ ์ธ์ฆ์ ํ์ํ ๋ชจ๋  ์ ๋ณด๋ ํ ํฐ ์์ฒด์ ํฌํจํ๊ธฐ ๋๋ฌธ์ ๋ณ๋์ ์ธ์ฆ ์ ์ฅ์๊ฐ ํ์์๋ค๋ ๊ฒ
๋ถ์ฐ ๋ง์ดํฌ๋ก ์๋น์ค ํ๊ฒฝ์์ ์ค์ ์ง์ค์ ์ธ์ฆ ์๋ฒ์ ๋ฐ์ดํฐ๋ฒ ์ด์ค์ ์์กดํ์ง ์๋ ์ฌ์ด ์ธ์ฆ ๋ฐ ์ธ๊ฐ ๋ฐฉ๋ฒ์ ์ ๊ณต
<br>
<br>

## ๐ AccessToken, RefreshToken์ ํ์ทจ ๋นํ๋ค๋ฉด?

๐ก **๋จผ์ , ์ดํด๋ฅผ ๋๊ธฐ์ํด ์๋์ ๊ฐ์ ์ํ์ด๋ค.**

1. ํด๋ผ์ด์ธํธ๊ฐ Access Token, Refresh Token ๋๋ค ๋ค๊ณ ์๋๋ค.
2. DB์ ๋ ๊ฐ์ ๋ชจ๋ ์ ์ฅํ๋ค.

**Q1. AccessToken์ ํ์ทจ๋นํ๋ฉด?**

AccessToken ์ ์งง์ ์ฃผ๊ธฐ๋ฅผ ๊ฐ์ง๋ฏ๋ก ํฐ ์ํ์ด ์๊ณ  Refresh Token์ ๋ค๊ณ ์์ง์์ ํด๋ผ์ด์ธํธ๋ ์ฌ๋ฐ๊ธ์ ๋ฐ์์ ์๋ค.

**Q2. RefreshToken์ ํ์ทจ๋นํ๋ฉด?**

์ ์์ ์ธ ์ฌ์ฉ์์ผ ๊ฒฝ์ฐ ๊ธฐ์กด์ AccessToken์ผ๋ก ์ ๊ทผํ๋ฉด DB์ ์ ์ฅ๋ AccessToken๊ณผ ๋น๊ตํ์ฌ ๊ฒ์ฆํ๋ค.

๊ณต๊ฒฉ์๋ ํ์ทจํ RefreshToken์ผ๋ก ์๋ก AccesToken์ ์์ฑํ์ฌ ์๋ฒ์ธก์ ์ ์กํ๋ฉด ์๋ฒ๋ DB์ ์ ์ฅ๋
AccesToken๊ณผ ๊ณต๊ฒฉ์์๊ฒ ๋ฐ์ AccesToken์ด ๋ค๋ฅธ๊ฒ์ ํ์ธํ๋ค.

๋ง์ฝ DB์ ์ ์ฅ๋ ํ ํฐ์ด ๋ง๋ฃ๋์ง ์์์ ๊ฒฝ์ฐ, (๊ตณ์ด AccessToken์ ์๋ก ์์ฑํ  ์ด์ ๊ฐ ์๋ ๊ฒฝ์ฐ)
์ด๋ ์๋ฒ๊ฐ RefreshToken์ ํ์ทจ ๋นํ๋ค๊ณ  ์๊ฐํ๊ณ  ๋ ํ ํฐ์ ํ๊ธฐ ์ํจ๋ค.


## ๐ ์ธ์ฆ๊ด๋ จ ์ํคํ์ณ
<img width="920" alt="์ธ์ฆ๊ด๋ จ_architecture" src="https://user-images.githubusercontent.com/56526225/191224926-8d699e20-cd5a-4cb2-8ea1-45a795e8ff15.png">
