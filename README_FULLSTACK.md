# Photography Blog Full-Stack Migration

This repo now contains a split frontend/backend version of the photography blog.

## Structure

- `frontend/`: Vue 3 + Vite + Element Plus + WangEditor
- `backend/`: Spring Boot + MyBatis Plus + MySQL
- Existing Hugo files are kept for reference and current GitHub Pages output.

## Frontend

```powershell
cd frontend
npm install
copy .env.example .env
npm run dev
```

Build for Cloudflare Pages:

```powershell
npm run build
```

Cloudflare Pages settings:

- Root directory: `frontend`
- Build command: `npm run build`
- Output directory: `dist`
- Environment variable: `VITE_API_BASE_URL=https://your-backend-domain.example/api`

The SPA fallback is configured in `frontend/public/_redirects`.

GitHub Pages is also configured through `.github/workflows/frontend-pages.yml`.
After the workflow runs, enable Pages in the GitHub repository settings:

- Source: `Deploy from a branch`
- Branch: `gh-pages`
- Folder: `/ (root)`

Then open:

- Homepage: `https://cx-lgtm.github.io/Photos/`
- Admin login: `https://cx-lgtm.github.io/Photos/admin/login`

Set the repository variable `VITE_API_BASE_URL` to your deployed backend URL so login, upload, comments, and article management can talk to Spring Boot.

## Backend

Install Java 17, Maven, and MySQL 8 first.

```powershell
cd backend
copy .env.example .env
mvn spring-boot:run
```

Create a MySQL database before the first run:

```sql
CREATE DATABASE photography_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

The backend runs `schema.sql` and `data.sql` by default. Set `SQL_INIT_MODE=never` after the first production setup if you prefer to manage schema manually.

Default local admin:

- Username: `admin`
- Password: `admin123`

Change `ADMIN_PASSWORD` and `JWT_SECRET` before deployment.

## Backend Deployment Notes

Render can build the backend with the included `backend/Dockerfile`. Set these environment variables:

```plain
DB_HOST
DB_PORT
DB_NAME
DB_USER
DB_PASSWORD
JWT_SECRET
ADMIN_USERNAME
ADMIN_PASSWORD
ADMIN_DISPLAY_NAME
CORS_ALLOWED_ORIGINS=https://your-cloudflare-pages-domain.pages.dev
UPLOAD_DIR=/data/uploads
SQL_INIT_MODE=always
```

For Oracle Cloud, run the same Docker image or install Java 17 and Maven on the VM. Put uploaded images on a persistent volume and point `UPLOAD_DIR` there.

## API Surface

Public:

- `GET /api/articles`
- `GET /api/articles/{slug}`
- `GET /api/categories`
- `GET /api/tags`
- `GET /api/comments`
- `POST /api/comments`
- `GET /api/profile`

Admin:

- `POST /api/admin/auth/login`
- `GET/POST/PUT/DELETE /api/admin/articles`
- `GET/POST/PUT/DELETE /api/admin/categories`
- `GET/POST/PUT/DELETE /api/admin/tags`
- `GET/PATCH/DELETE /api/admin/comments`
- `GET/POST/DELETE /api/admin/images`
